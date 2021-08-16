{ mkDerivation, aeson, base, blaze-html, blaze-markup, dhall
, fused-effects, lens, lib, mtl, servant-blaze, servant-server, stm
, tasty, tasty-hunit, tasty-quickcheck, text, time, wai, wai-extra
, warp
}:
mkDerivation {
  pname = "reviews-next";
  version = "0.1.0.0";
  src = ./.;
  isLibrary = true;
  isExecutable = true;
  libraryHaskellDepends = [
    aeson base blaze-html blaze-markup dhall fused-effects lens mtl
    servant-blaze servant-server stm text time
  ];
  executableHaskellDepends = [ base wai wai-extra warp ];
  testHaskellDepends = [ base tasty tasty-hunit tasty-quickcheck ];
  homepage = "https://github.com/nilenso/reviews-next#readme";
  license = lib.licenses.bsd3;
}
