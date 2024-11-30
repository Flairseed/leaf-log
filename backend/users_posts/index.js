const { response } = require("./functions/response-functions");
const {
  connectToDatabase,
  userRegister,
  userLogin,
} = require("./functions/database-functions");

exports.handler = async (event, context) => {
  context.callbackWaitsForEmptyEventLoop = false;
  await connectToDatabase();

  let body;
  let res;
  let userId;
  let id;

  switch (event.routeKey) {
    case "POST /users/register":
      body = JSON.parse(event.body);
      res = await userRegister(body);
      break;
    case "POST /users/login":
      body = JSON.parse(event.body);
      res = await userLogin(body);
      break;
    default:
      res = response(404, { message: "Unsupported route: " + event.routeKey });
  }
  return res;
};
