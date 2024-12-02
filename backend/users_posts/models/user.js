const Ajv = require("ajv");
const ajv = new Ajv({ allErrors: true });

const user = {
  type: "object",
  properties: {
    name: {
      type: "string",
      minLength: 1,
      maxLength: 256,
    },
    password: {
      type: "string",
      minLength: 10,
      maxLength: 256,
    },
  },
  required: ["name", "password"],
  additionalProperties: false,
};

const validateUser = ajv.compile(user);

module.exports = { validateUser };
