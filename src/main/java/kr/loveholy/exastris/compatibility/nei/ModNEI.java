package kr.loveholy.exastris.compatibility.nei;

import codechicken.nei.api.API;

public class ModNEI {
	public static void init()
	{

		RecipeHandlerSieve handlerSieve = new RecipeHandlerSieve();
		RecipeHandlerHammer handlerHammer = new RecipeHandlerHammer();
		API.registerUsageHandler(handlerSieve);
		API.registerRecipeHandler(handlerSieve);
		API.registerUsageHandler(handlerHammer);
		API.registerRecipeHandler(handlerHammer);
	}
}
