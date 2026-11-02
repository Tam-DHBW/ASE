{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = inputs: {
    devShells = builtins.mapAttrs (system: pkgs: {
      default = pkgs.mkShell {
        packages = with pkgs; [
          jdk25
          maven
          jdt-language-server
        ];

        JAVA_HOME = pkgs.jdk25;

        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath (with pkgs; [
          glib
          libGL
          libxxf86vm
          libxtst
        ]);
      };
    }) inputs.nixpkgs.legacyPackages;
  };
}
