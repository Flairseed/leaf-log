function response(status, body) {
  return {
    statusCode: status,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  };
}

function displayErrors(errors) {
  const errorList = [];
  for (const error of errors) {
    let property;
    if (error.params.missingProperty) {
      property = `/${error.params.missingProperty}`;
    } else {
      property = error.instancePath;
    }
    errorList.push(`${property}: ${error.message}`);
  }
  return {
    message: "There were some errors in your request body",
    errors: errorList,
  };
}

module.exports = { response, displayErrors };
