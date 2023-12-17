package absloader.example;

import absloader.library.Mod;

public class ExampleMod {

	@Mod(
			id = "example_mod",
			name = "Example Mod",
			description = "This is a description of an auto-generated mod.",
			authors = {"HexagonNico"},
			credits = "Credits go here. You're welcome.",
			homepage = "https://example.com/homepage",
			sources = "https://github.com",
			issues = "https://example.com/issueTracker",
			license = "CC BY-NC-ND 4.0"
	)
	public static void initialize() {

	}
}
