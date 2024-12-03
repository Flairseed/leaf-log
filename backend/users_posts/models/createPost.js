const Ajv = require("ajv");
const { addFormats } = require("ajv-formats");

const ajv = new Ajv({ allErrors: true });
addFormats(ajv);

const createPost = {
  type: "object",
  properties: {
    userId: {
      type: "integer",
    },
    title: {
      type: "string",
      minLength: 1,
      maxLength: 256,
    },
    description: {
      type: "string",
      minLength: 1,
    },
    title: {
      type: "string",
      minLength: 1,
      maxLength: 256,
    },
    body: {
      type: "string",
      minLength: 1,
    },
    height: {
      type: "number",
      maximum: 9999.99,
    },
    water: {
      type: "integer",
    },
    lightLevel: {
      type: "number",
      maximum: 999999.99,
    },
    relativeHumidity: {
      type: "integer",
      maximum: 100,
    },
    temperature: {
      type: "integer",
    },
    picture: {
      type: "string",
      maxLength: "256",
      format: "uri",
    },
    created: {
      type: "string",
      format: "date",
    },
  },
  required: [
    userId,
    title,
    description,
    height,
    water,
    lightLevel,
    relativeHumidity,
    temperature,
    picture,
  ],
  additionalProperties: false,
};

const validateCreatePost = ajv.compile(createPost);

module.exports = { validateCreatePost };
