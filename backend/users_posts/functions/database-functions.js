const mysql = require("mysql2/promise");
const { response, displayErrors } = require("./response-functions");
const { validateUser } = require("../models/user");
const { validateCreatePost } = require("../models/createPost");
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

    await connection.query(sql, queryValues);
    return response(200, {
      message: "Successfully registered.",
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
    return response(400, displayErrors(validateUser.errors));
  }

  try {
    const sql =
      "INSERT INTO post(user_id, title, description, height, water, light_level, relative_humidity, temperature, picture, created) " +
      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    const queryValues = [
      req.user_id,
      req.title,
      req,
      description,
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

module.exports = {
  connectToDatabase,
  userRegister,
  userLogin,
  createPost,
};
