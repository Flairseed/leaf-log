
exports.handler = async (event, context) => {
  context.callbackWaitsForEmptyEventLoop = false;

  let body;
  let res;
  let userId;
  let id;

  switch (event.routeKey) {
    case "POST users/register":
      break;
    case "POST users/login":
      break;
  }
}