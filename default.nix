{ nixpkgs ? import <nixpkgs> {}, compiler ? "ghc901" }:
nixpkgs.pkgs.haskell.packages.${compiler}.callPackage ./reviews-next.nix { }
