const mysql = require("mysql2/promise");
const { response, displayErrors } = require("./response-functions");
const { validateUser } = require("../models/user");
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
    const sql = "INSERT INTO user(name, pasword) VALUES(?, ?)";
    const queryValues = [req.name, req.pasword];

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
    const sql = "SELECT * FROM user WHERE name = ?";
    const [users, fields] = await connection.query(sql, [req.name]);
    if (users.length === 0 || users[0].password !== req.password) {
      return response(403, {
        message: "Username or password is incorrect",
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

module.exports = {
  connectToDatabase,
  userRegister,
  userLogin,
};
