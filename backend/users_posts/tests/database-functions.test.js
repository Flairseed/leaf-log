const {
  connectToDatabase,
  userRegister,
  userLogin,
} = require("../functions/database-functions");
const mysql2 = require("mysql2");

let mockQuery = jest.fn();
jest.mock("mysql2/promise", () => ({
  createConnection: () => ({
    query: mockQuery,
  }),
}));

beforeAll(() => {
  connectToDatabase();
});

describe("userRegister", () => {
  it("should successfully register a new user that is not already registered.", async () => {
    const testData = []; // Empty because there should be no record of a John in the database
    const req = {
      name: "John",
      password: "password123",
    };

    mockQuery.mockImplementationOnce((sql, name) => [testData, {}]);

    // Success response
    expect(await userRegister(req)).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Successfully registered.",
      }),
    });

    // Check if called with correct parameters both times
    expect(mockQuery).toHaveBeenNthCalledWith(
      1,
      "SElECT * FROM user WHERE name = ?",
      [req.name]
    );
    expect(mockQuery).toHaveBeenNthCalledWith(
      2,
      "INSERT INTO user(name, password) VALUES(?, ?)",
      [req.name, req.password]
    );
  });

  it("should fail if user is already registered", async () => {
    const testData = [
      {
        id: 1,
        name: "John",
        password: "password123",
      },
    ];
    const req = {
      name: "John",
      password: "password123",
    };

    mockQuery.mockImplementationOnce((sql, name) => [testData, {}]);

    // Error response
    expect(await userRegister(req)).toEqual({
      statusCode: 403,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `User with name ${req.name} is already registered.`,
      }),
    });

    expect(mockQuery).toHaveBeenNthCalledWith(
      1,
      "SElECT * FROM user WHERE name = ?",
      [req.name]
    );
    // Main function should have returned early due to the error.
    expect(mockQuery).toHaveBeenCalledTimes(1);
  });

  it("should fail immediately if user inputs with the wrong format", async () => {
    // Password cannot be less than 10 characters
    const req = {
      name: "John",
      password: "password",
    };

    expect(await userRegister(req)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "There were some errors in your request body",
        errors: ["/password: must NOT have fewer than 10 characters"],
      }),
    });

    // Main function should have returned before any database calls.
    expect(mockQuery).toHaveBeenCalledTimes(0);
  });
});

describe("userLogin", () => {
  it("should succesffully login a user that gives the correct username and password.", async () => {
    const testData = [
      {
        id: 1,
        name: "John",
        password: "password123",
      },
    ];
    const req = {
      name: "John",
      password: "password123",
    };

    mockQuery.mockImplementationOnce((sql, name) => [testData, {}]);

    expect(await userLogin(req)).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Successfully logged in.",
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith(
      "SELECT * FROM user WHERE name = ? AND password = ?",
      [req.name, req.password]
    );
  });

  it("should fail if user provides incorrect name or password", async () => {
    const testData = []; // Empty because no records should match the incorrect credentials
    const req = {
      name: "John",
      password: "password124",
    };

    mockQuery.mockImplementationOnce((sql, name) => [testData, {}]);

    expect(await userLogin(req)).toEqual({
      statusCode: 403,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Username or password is incorrect.",
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith(
      "SELECT * FROM user WHERE name = ? AND password = ?",
      [req.name, req.password]
    );
  });

  it("should fail immediately if user inputs with the wrong format", async () => {
    // Name must be provided and password cannot be less than 10 characters
    const req = {
      password: "password",
    };

    expect(await userLogin(req)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "There were some errors in your request body",
        errors: [
          "/name: must have required property 'name'",
          "/password: must NOT have fewer than 10 characters",
        ],
      }),
    });

    // Main function should have returned before any database calls.
    expect(mockQuery).toHaveBeenCalledTimes(0);
  });
});
