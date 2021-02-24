{ mkDerivation, acid-state, base, blaze-html, bytestring, dhall
, lens, mtl, safecopy, servant-blaze, servant-server, stdenv, text
, time, wai, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = false;
  isExecutable = true;
  executableHaskellDepends = [
    acid-state base blaze-html bytestring dhall lens mtl safecopy
    servant-blaze servant-server text time wai warp
  ];
  description = "Webapp for internal nilenso reviews";
  license = "unknown";
  hydraPlatforms = stdenv.lib.platforms.none;
}
