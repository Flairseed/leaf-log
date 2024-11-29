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