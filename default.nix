{ mkDerivation, acid-state, base, blaze-html, dhall, lens, mtl
, safecopy, servant, stdenv, text, time, wai, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = false;
  isExecutable = true;
  executableHaskellDepends = [
    acid-state base blaze-html dhall lens mtl safecopy servant text
    time wai warp
  ];
  description = "Webapp for internal nilenso reviews";
  license = "unknown";
  hydraPlatforms = stdenv.lib.platforms.none;
}
