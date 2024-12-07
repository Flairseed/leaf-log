const { response } = require("./functions/response-functions");
const {
  connectToDatabase,
  userRegister,
  userLogin,
  createPost,
  updatePost,
  deletePost,
  getPosts,
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
    case "POST /posts":
      body = JSON.parse(event.body);
      res = await createPost(body);
      break;
    case "PUT /posts/{id}":
      id = event.pathParameters.id;
      body = JSON.parse(event.body);
      res = await updatePost(id, body);
      break;
    case "DELETE /posts/{id}":
      id = event.pathParameters.id;
      userId = event.queryStringParameters.userId;
      res = await deletePost(userId, id);
      break;
    case "GET /posts":
      res = await getPosts();
      break;
    default:
      res = response(404, { message: "Unsupported route: " + event.routeKey });
  }
  return res;
};
