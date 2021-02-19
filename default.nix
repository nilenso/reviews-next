{ mkDerivation, base, servant, stdenv }:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = false;
  isExecutable = true;
  executableHaskellDepends = [ base servant ];
  description = "Webapp for internal nilenso reviews";
  license = "unknown";
  hydraPlatforms = stdenv.lib.platforms.none;
}
