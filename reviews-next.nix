{ mkDerivation, base, blaze-html, dhall, hoauth2, lib
, servant-blaze, servant-server, text, wai, wai-extra, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = false;
  isExecutable = true;
  executableHaskellDepends = [
    base blaze-html dhall hoauth2 servant-blaze servant-server text wai
    wai-extra warp
  ];
  license = "unknown";
  hydraPlatforms = lib.platforms.none;
}
