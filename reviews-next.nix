{ mkDerivation, aeson, base, blaze-html, bytestring, containers
, dhall, fused-effects, hoauth2, http-api-data, http-conduit
, http-types, jwt, lens, lib, mtl, safecopy, servant-auth-server
, servant-blaze, servant-server, stm, tasty, tasty-hunit
, tasty-quickcheck, tasty-wai, text, time, uri-bytestring, uuid
, wai, wai-extra, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = true;
  isExecutable = true;
  libraryHaskellDepends = [
    aeson base blaze-html bytestring containers dhall fused-effects
    hoauth2 http-conduit jwt lens mtl safecopy servant-auth-server
    servant-blaze servant-server stm text time uri-bytestring uuid
  ];
  executableHaskellDepends = [ base wai wai-extra warp ];
  testHaskellDepends = [
    aeson base containers fused-effects http-api-data http-conduit
    http-types lens mtl servant-server stm tasty tasty-hunit
    tasty-quickcheck tasty-wai text uri-bytestring wai wai-extra warp
  ];
  license = "unknown";
  hydraPlatforms = lib.platforms.none;
}
