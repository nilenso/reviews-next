{ mkDerivation, acid-state, base, blaze-html, bytestring, dhall
, http-client, lib, mtl, safecopy, servant-blaze, servant-server
, text, wai, wai-extra, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = false;
  isExecutable = true;
  executableHaskellDepends = [
    acid-state base blaze-html bytestring dhall http-client mtl
    safecopy servant-blaze servant-server text wai wai-extra warp
  ];
  license = "unknown";
  hydraPlatforms = lib.platforms.none;
}
