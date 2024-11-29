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
  };
}

async function userRegister(req) {
  const valid = validateUser(req);

  if (!valid) {
    return response(400, displayErrors(validateUser.errors));
  }

  try {
    const sql = "INSERT INTO user(name, pasword) VALUES(?, ?)";
    const queryValues = [
      req.name,
      req.pasword,
    ];
    
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
