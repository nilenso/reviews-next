{ port = 3000,
  assetsDir = "./",
  dataDir = "./",
  secrets = { keyBase = "TEST_KEY_BASE" },
  githubOAuthConfig = {
    clientId = "TEST_CLIENT_ID",
    clientSecret = Some "TEST_CLIENT_SECRET",
    authorizeEndpoint = "http://localhost:3005/login/oauth/authorize",
    accessTokenEndpoint = "http://localhost:3005/login/oauth/access_token",
    callback = Some "http://localhost:3000/oauth/callback/github",
    userDetailsUri = "http://localhost:3005/user"
  }
}
