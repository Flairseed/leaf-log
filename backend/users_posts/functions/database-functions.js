const mysql = require("mysql2/promise");
const { response, displayErrors } = require("./response-functions");
const { validateUser } = require("../models/user");
const { validateCreatePost } = require("../models/createPost");
const { S3Client, PutObjectCommand } = require("@aws-sdk/client-s3");
const { getSignedUrl } = require("@aws-sdk/s3-request-presigner");

require("dotenv").config();

let connection = null;

async function connectToDatabase() {
  if (connection === null) {
    connection = await mysql.createConnection({
      host: process.env.HOST_NAME,
      user: process.env.USER_NAME,
      password: process.env.PASSWORD,
      port: process.env.PORT,
      database: process.env.DATABASE,
    });
  }
}

async function userRegister(req) {
  const valid = validateUser(req);

  if (!valid) {
    return response(400, displayErrors(validateUser.errors));
  }

  try {
    const findSql = "SElECT * FROM user WHERE name = ?";
    const [users, fields] = await connection.query(findSql, [req.name]);
    if (users.length !== 0) {
      return response(403, {
        message: `User with name ${req.name} is already registered.`,
      });
    }

    const sql = "INSERT INTO user(name, password) VALUES(?, ?)";
    const queryValues = [req.name, req.password];

    const [results] = await connection.query(sql, queryValues);
    return response(200, {
      message: "Successfully registered.",
      body: {
        id: results.insertId
      }
    });
  } catch (err) {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function userLogin(req) {
  const valid = validateUser(req);

  if (!valid) {
    return response(400, displayErrors(validateUser.errors));
  }

  try {
    const sql = "SELECT * FROM user WHERE name = ? AND password = ?";
    const queryValues = [req.name, req.password];

    const [users, fields] = await connection.query(sql, queryValues);
    if (users.length === 0) {
      return response(403, {
        message: "Username or password is incorrect.",
      });
    } else {
      return response(200, {
        message: "Successfully logged in.",
        body: {
          id: users[0].id
        }
      });
    }
  } catch (err) {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function createPost(req) {
  const valid = validateCreatePost(req);

  if (!valid) {
    return response(400, displayErrors(validateCreatePost.errors));
  }

  try {
    const sql =
      "INSERT INTO post(user_id, title, description, height, water, light_level, relative_humidity, temperature, picture, created) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    const queryValues = [
      req.userId,
      req.title,
      req.description,
      req.height,
      req.water,
      req.lightLevel,
      req.relativeHumidity,
      req.temperature,
      req.picture,
      req.created,
    ];

    await connection.query(sql, queryValues);
    return response(200, {
      message: "Successfully created post.",
    });
  } catch (err) {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function updatePost(postId, req) {
  if (!Number.isInteger(+postId)) {
    return response(400, {
      message: `${postId} is not a valid post id. Post id must be an integer.`,
    });
  }

  const selectSql = "SELECT * FROM post WHERE id = ?";
  const [posts, fields] = await connection.query(selectSql, [postId]);
  if (posts.length === 0) {
    return response(404, {
      message: `Post with id of ${postId} does not exist.`,
    });
  } else if (posts[0].user_id !== +req.userId) {
    return response(403, {
      message: `User with id of ${req.userId} is not allowed to edit post of id ${postId}.`,
    });
  }

  const valid = validateCreatePost(req);

  if (!valid) {
    return response(400, displayErrors(validateCreatePost.errors));
  }

  try {
    const updateSql =
      "UPDATE post SET user_id = ?, title = ?, description = ?, height = ?, water = ?, light_level = ?, " + 
      "relative_humidity = ?, temperature = ?, picture = ?, created = ? WHERE id = ?";
    const queryValues = [
      req.userId,
      req.title,
      req.description,
      req.height,
      req.water,
      req.lightLevel,
      req.relativeHumidity,
      req.temperature,
      req.picture,
      req.created,
      postId,
    ];

    await connection.query(updateSql, queryValues);

    return response(200, {
      message: "Post successfully updated.",
    });
  } catch (err) {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function deletePost(userId, postId) {
  if (!Number.isInteger(+userId)) {
    return response(400, {
      message: `${userId} is not a valid user id. User id must be an integer.`,
    });
  }

  if (!Number.isInteger(+postId)) {
    return response(400, {
      message: `${postId} is not a valid post id. Post id must be an integer.`,
    });
  }

  const selectSql = "SELECT * FROM post WHERE id = ?";
  const [posts, fields] = await connection.query(selectSql, [postId]);
  if (posts.length === 0) {
    return response(404, {
      message: `Post with id of ${postId} does not exist.`,
    });
  } else if (posts[0].user_id !== +userId) {
    return response(403, {
      message: `User with id of ${userId} is not allowed to delete post of id ${postId}.`,
    });
  }

  try {
    const deleteSql = "DELETE FROM post WHERE id = ?";

    await connection.query(deleteSql, [postId]);

    return response(200, {
      message: "Post successfully deleted.",
    });
  } catch (err) {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function getPosts() {
  try {
    sql = "SELECT post.*, user.name FROM post JOIN user ON user.id = post.user_id;";
    const [posts, fields] = await connection.query(sql);
    return response(200, {
      message: "Successfully retrieved posts.",
      body: posts,
    });
  } catch {
    console.log(err);
    return response(500, {
      message: "Internal server error.",
    });
  }
}

async function getPresignedUrl(userName = null) {
  try {
    const s3 = new S3Client();
    const s3Url = process.env.S3_URL;

    const currentTime = Date.now();
    const folder = process.env.S3_FOLDER;
    const key = `${folder}/${userName}-${currentTime}.png`;

    const command = new PutObjectCommand({ Bucket: process.env.BUCKET_NAME, Key: key });
    const presignedUrl = await getSignedUrl(s3, command, { expiresIn: 3600 });

    const imagePath = `${s3Url}${key}`;

    return response(200, {
      message: "You have successfully generated a presigned s3 url",
      body: {
        url: presignedUrl,
        imagePath: imagePath
      }
    });
  } catch(err) {
    console.log(err);
    return response(500, {
      message: "Internal server error."
    });
  }
}

module.exports = {
  connectToDatabase,
  userRegister,
  userLogin,
  createPost,
  updatePost,
  deletePost,
  getPosts,
  getPresignedUrl
};
