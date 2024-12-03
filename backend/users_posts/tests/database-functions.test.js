const {
  connectToDatabase,
  userRegister,
  userLogin,
  createPost,
  updatePost,
  deletePost,
  getPosts,
} = require("../functions/database-functions");

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

describe("createPost", () => {
  it("should successfully create a post when given a properly formatted input", async () => {
    const req = {
      userId: 1,
      title: "day one",
      description: "The plant is growing well.",
      height: 0,
      water: 50,
      lightLevel: 10000,
      relativeHumidity: 87,
      temperature: 30,
      picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      created: "2024-12-04",
    };

    const argInputs = [
      1,
      "day one",
      "The plant is growing well.",
      0,
      50,
      10000,
      87,
      30,
      "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      "2024-12-04",
    ];

    expect(await createPost(req)).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Successfully created post.",
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith(
      "INSERT INTO post(user_id, title, description, height, water, light_level, relative_humidity, temperature, picture, created) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
      argInputs
    );
  });

  it("should fail immediately if given format is wrong", async () => {
    // userId should be provided
    const req = {
      title: "day one",
      description: "The plant is growing well.",
      height: 0,
      water: 50,
      lightLevel: 10000,
      relativeHumidity: 87,
      temperature: 30,
      picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      created: "2024-12-04",
    };

    expect(await createPost(req)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "There were some errors in your request body",
        errors: ["/userId: must have required property 'userId'"],
      }),
    });

    // Main function should have returned before any database calls.
    expect(mockQuery).toHaveBeenCalledTimes(0);
  });
});

describe("updatePost", () => {
  it("should successfully update the post given a properly formatted input.", async () => {
    // Should return user's existing post
    const testData = [
      {
        user_id: 1,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
      },
    ];

    const req = {
      userId: 1,
      title: "day one",
      description: "The plant is growing well.",
      height: 0,
      water: 50,
      lightLevel: 10000,
      relativeHumidity: 87,
      temperature: 30,
      picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      created: "2024-12-04",
    };
    postId = 1;

    const argInputs = [
      1,
      "day one",
      "The plant is growing well.",
      0,
      50,
      10000,
      87,
      30,
      "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      "2024-12-04",
      postId,
    ];

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]); // Checking if post exists

    expect(await updatePost(postId, req)).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Post successfully updated.",
      }),
    });

    // Checking if post exists
    expect(mockQuery).toHaveBeenNthCalledWith(
      1,
      "SELECT * FROM post WHERE id = ?",
      [postId]
    );

    // Updating the post
    expect(mockQuery).toHaveBeenNthCalledWith(
      2,
      "UPDATE post SET user_id = ?, title = ?, description = ?, height = ?, water = ?, light_level = ?, " +
        "relative_humidity = ?, temperature = ?, picture = ?, created = ? WHERE id = ?",
      argInputs
    );
  });

  it("should fail immediately with wrongly formatted postId", async () => {
    // postId should be an integer
    postId = "dawdohe233";

    expect(await updatePost(postId, {})).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `${postId} is not a valid post id. Post id must be an integer.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledTimes(0); // Main function should have returned early
  });

  it("should fail updating if post with given id does not exist", async () => {
    // Empty because post with given id does not exist
    testData = [];
    postId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]);

    expect(await updatePost(postId, {})).toEqual({
      statusCode: 404,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `Post with id of ${postId} does not exist.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith("SELECT * FROM post WHERE id = ?", [
      postId,
    ]);

    expect(mockQuery).toHaveBeenCalledTimes(1); // Update query should not have been called
  });

  it("Should fail updating if user id of post does not match given user id", async () => {
    const testData = [
      {
        user_id: 2,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
      },
    ];

    const req = {
      userId: 1,
      title: "day one",
      description: "The plant is growing well.",
      height: 0,
      water: 50,
      lightLevel: 10000,
      relativeHumidity: 87,
      temperature: 30,
      picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      created: "2024-12-04",
    };
    postId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]);

    expect(await updatePost(postId, req)).toEqual({
      statusCode: 403,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `User with id of ${req.userId} is not allowed to edit post of id ${postId}.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith("SELECT * FROM post WHERE id = ?", [
      postId,
    ]);

    expect(mockQuery).toHaveBeenCalledTimes(1); // Update query should not have been called
  });

  it("Should fail to update if format of input is wrong", async () => {
    const testData = [
      {
        user_id: 1,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
      },
    ];

    // Date format should be yyyy-mm-dd
    const req = {
      userId: 1,
      title: "day one",
      description: "The plant is growing well.",
      height: 0,
      water: 50,
      lightLevel: 10000,
      relativeHumidity: 87,
      temperature: 30,
      picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
      created: "4/12/2024",
    };
    postId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]);

    expect(await updatePost(postId, req)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "There were some errors in your request body",
        errors: ['/created: must match format "date"'],
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith("SELECT * FROM post WHERE id = ?", [
      postId,
    ]);

    expect(mockQuery).toHaveBeenCalledTimes(1); // Update query should not have been called
  });
});

describe("deletePost", () => {
  it("should successfully delete post if given user id and post id is correct", async () => {
    const testData = [
      {
        user_id: 1,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
      },
    ];
    postId = 1;
    userId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]); // Checking if post exists

    expect(await deletePost(userId, postId)).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Post successfully deleted.",
      }),
    });

    // Checking if post exists
    expect(mockQuery).toHaveBeenNthCalledWith(
      1,
      "SELECT * FROM post WHERE id = ?",
      [postId]
    );

    // Deleting the post
    expect(mockQuery).toHaveBeenNthCalledWith(
      2,
      "DELETE FROM post WHERE id = ?",
      [postId]
    );
  });

  it("should fail immediately with wrongly formatted userId", async () => {
    // userId should be an integer
    userId = "dawdohe233";

    expect(await deletePost(userId, 1)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `${userId} is not a valid user id. User id must be an integer.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledTimes(0); // Main function should have returned early
  });

  it("should fail immediately with wrongly formatted postId", async () => {
    // postId should be an integer
    postId = "dawdohe233";

    expect(await deletePost(1, postId)).toEqual({
      statusCode: 400,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `${postId} is not a valid post id. Post id must be an integer.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledTimes(0); // Main function should have returned early
  });

  it("should fail deleting if post with given id does not exist", async () => {
    // Empty because post with given id does not exist
    testData = [];
    postId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]);

    expect(await deletePost(1, postId)).toEqual({
      statusCode: 404,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `Post with id of ${postId} does not exist.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith("SELECT * FROM post WHERE id = ?", [
      postId,
    ]);

    expect(mockQuery).toHaveBeenCalledTimes(1); // Delete query should not have been called
  });

  it("Should fail deleting if user id of post does not match given user id", async () => {
    const testData = [
      {
        user_id: 2,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
      },
    ];

    userId = 1;
    postId = 1;

    mockQuery.mockImplementationOnce((sql, postId) => [testData, {}]);

    expect(await deletePost(userId, postId)).toEqual({
      statusCode: 403,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: `User with id of ${userId} is not allowed to delete post of id ${postId}.`,
      }),
    });

    expect(mockQuery).toHaveBeenCalledWith("SELECT * FROM post WHERE id = ?", [
      postId,
    ]);

    expect(mockQuery).toHaveBeenCalledTimes(1); // Delete query should not have been called
  });
});

describe("getPosts", () => {
  it("should return all the posts in the database", async () => {
    testData = [
      {
        user_id: 1,
        title: "day one",
        description: "The plant is growing well.",
        height: 0,
        water: 20,
        light_level: 5000,
        relative_humidity: 50,
        temperature: 30,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
        time_stamp: "2024-12-04T23:59:60Z",
      },
      {
        user_id: 2,
        title: "day one",
        description: "The plant is NOT growing well.",
        height: 0,
        water: 4,
        light_level: 10000,
        relative_humidity: 90,
        temperature: 34,
        picture: "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg",
        created: "2024-12-04",
        time_stamp: "2024-12-04T23:59:60Z",
      },
    ];

    mockQuery.mockImplementationOnce((sql) => [testData, {}]);

    expect(await getPosts()).toEqual({
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        message: "Successfully retrieved posts.",
        body: testData,
      }),
    });
  });
});
