package kr.loveholy.exastris.compatibility.nei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.awt.Point;
import java.awt.Rectangle;

import kr.loveholy.exastris.compatibility.nei.util.ItemInfo;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo2.registries.sifting.SieveRegistry;
import exnihilo2.registries.sifting.SieveRegistryEntry;
import exnihilo2.registries.sifting.SieveReward;
import exnihilo2.util.enums.EnumMetadataBehavior;

public class RecipeHandlerSieve extends TemplateRecipeHandler {

	private static final int SLOTS_PER_PAGE = 45;

	public class CachedSieveRecipe extends CachedRecipe {

		private List<PositionedStack> input = new ArrayList<PositionedStack>();
		private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public Point focus;

		public CachedSieveRecipe(List<ItemStack> variations, ItemStack base,
				ItemStack focus) {
			PositionedStack pstack = new PositionedStack(base != null ? base
					: variations, 74, 4);
			pstack.setMaxSize(1);
			this.input.add(pstack);

			int row = 0;
			int col = 0;
			for (ItemStack v : variations) {
				this.outputs.add(new PositionedStack(v, 3 + 18 * col,
						37 + 18 * row));

				if (focus != null
						&& NEIServerUtils.areStacksSameTypeCrafting(focus, v)) {
					this.focus = new Point(2 + 18 * col, 36 + 18 * row);
				}

				col++;
				if (col > 8) {
					col = 0;
					row++;
				}
			}
		}

		public CachedSieveRecipe(List<ItemStack> variations) {
			this(variations, null, null);
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return this.getCycledIngredients(cycleticks / 20, this.input);
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			return this.outputs;
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}

	}

	@Override
	public String getRecipeName() {
		return "Ex Nihilo Sieve";
	}

	@Override
	public String getGuiTexture() {
		return "exastris:textures/gui/sieveNEI.png";
	}

	private void addCached(List<ItemStack> variations, ItemStack base,
			ItemStack focus) {
		if (variations.size() > SLOTS_PER_PAGE) {
			List<List<ItemStack>> parts = new ArrayList<List<ItemStack>>();
			int size = variations.size();
			for (int i = 0; i < size; i += SLOTS_PER_PAGE) {
				parts.add(new ArrayList<ItemStack>(variations.subList(i,
						Math.min(size, i + SLOTS_PER_PAGE))));
			}
			for (List<ItemStack> part : parts) {
				this.arecipes.add(new CachedSieveRecipe(part, base, focus));
			}
		} else {
			this.arecipes.add(new CachedSieveRecipe(variations, base, focus));
		}
	}

	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);

		Point focus = ((CachedSieveRecipe) this.arecipes.get(recipeIndex)).focus;
		if (focus != null) {
			GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
		}
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void loadTransferRects() {
		this.transferRects
				.add(new TemplateRecipeHandler.RecipeTransferRect(
						new Rectangle(75, 22, 15, 13), "exnihilo.sieve",
						new Object[0]));
	}

	
	@Override
	public void loadCraftingRecipes(String outputID, Object... results) {
		if (outputID.equals("exnihilo.sieve")) {
			for (Entry<String, SieveRegistryEntry> ii : SieveRegistry.getEntryMap().entrySet()) {
				ItemStack inputStack = new ItemStack(ii.getValue().getInput().getBlock());
				ArrayList<ItemStack> resultStack = new ArrayList<ItemStack>();
				HashMap<ItemInfo, Integer> cache = new HashMap<ItemInfo, Integer>();
				for (SieveReward s : SieveRegistry.getEntryForBlockState(ii.getValue().getInput(), EnumMetadataBehavior.SPECIFIC).getRewards()) {
					ItemInfo currInfo = new ItemInfo(s.getItem());
					if (cache.containsKey(currInfo))
						cache.put(currInfo, cache.get(currInfo)+1);
					else
						cache.put(currInfo,  1);
				}
				for (ItemInfo outputInfos : cache.keySet())
				{
					ItemStack stack = outputInfos.getItemStack();
					stack.stackSize = cache.get(outputInfos);
					resultStack.add(stack);
				}
				addCached(resultStack, inputStack, null);
			}
		} else
			super.loadCraftingRecipes(outputID, results);
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack result) {
		HashSet<SieveRegistryEntry> completed = new HashSet<SieveRegistryEntry>();
		
		ArrayList<SieveRegistryEntry> res = new ArrayList<SieveRegistryEntry>();

		for (Entry<String, SieveRegistryEntry> entry : SieveRegistry.getEntryMap().entrySet())
		{
			for (SieveReward sift : entry.getValue().getRewards())
			{
				if (result.isItemEqual(sift.getItem())) res.add(entry.getValue());
			}


		}
		
		
		for (SieveRegistryEntry ii : res)
		{
			if (!completed.contains(ii))
			{
				HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();
				for (SieveReward results : SieveRegistry.getEntryForBlockState(ii.getInput().getBlock().getBlockState().getBaseState(), EnumMetadataBehavior.SPECIFIC).getRewards())
				{
					ItemInfo current = new ItemInfo(results.getItem());
					if (stored.containsKey(current))
						stored.put(current, stored.get(current)+1);
					else
						stored.put(current, 1);
				}
				ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
				for (ItemInfo info : stored.keySet())
				{
					ItemStack stack = info.getItemStack();
					stack.stackSize = stored.get(info);
					resultVars.add(stack);
				}
				addCached(resultVars, new ItemStack(ii.getInput().getBlock()), result);
				completed.add(ii);
			}
		}
		 

	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		HashMap<ItemInfo, Integer> stored = new HashMap<ItemInfo, Integer>();

		if (Block.getBlockFromItem(ingredient.getItem()) == null || Block.getBlockFromItem(ingredient.getItem()) == Blocks.air)
			return;

		if (!SieveRegistry.isSiftable(Block.getBlockFromItem(ingredient.getItem()).getBlockState().getBaseState())) {
			return;
		}

		for (SieveReward results : SieveRegistry.getEntryForBlockState(Block.getBlockFromItem(ingredient.getItem()).getBlockState().getBaseState(), EnumMetadataBehavior.SPECIFIC).getRewards()) {
			ItemInfo current = new ItemInfo(results.getItem());
			
			if (stored.containsKey(current))
				stored.put(current, stored.get(current) + 1);
			else
				stored.put(current, 1);
		}
		ArrayList<ItemStack> resultVars = new ArrayList<ItemStack>();
		for (ItemInfo info : stored.keySet()) {
			ItemStack stack = info.getItemStack();
			stack.stackSize = stored.get(info);
			resultVars.add(stack);
		}
		addCached(resultVars, ingredient, ingredient);
	}

	@SuppressWarnings("unused")
	private void addCached(List<ItemStack> variations) {
		addCached(variations, null, null);
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe guiRecipe,
			ItemStack itemStack, List<String> currenttip, int recipe) {
		super.handleItemTooltip(guiRecipe, itemStack, currenttip, recipe);
		CachedSieveRecipe crecipe = (CachedSieveRecipe) this.arecipes
				.get(recipe);
		Point mouse = GuiDraw.getMousePosition();
		Point offset = guiRecipe.getRecipePosition(recipe);
		Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2
				- offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);

		if (itemStack != null && relMouse.y > 34) {
			currenttip.add("Drop Chance:");
			ItemStack sourceStack = crecipe.input.get(0).item;
			Block inBlock = Block.getBlockFromItem(sourceStack.getItem());
			int meta = sourceStack.getItemDamage();
			for (SieveReward smash : SieveRegistry.getEntryForBlockState(inBlock.getBlockState().getBaseState(), EnumMetadataBehavior.SPECIFIC).getRewards()) {
				if (NEIServerUtils.areStacksSameTypeCrafting(itemStack,
						new ItemStack(smash.getItem().getItem(), 1, smash.getItem().getItemDamage()))) {
					int chance = (int) smash.getBaseChance();
					currenttip.add("  * " + Integer.toString(chance) + "%");

				}

			}

		}
		return currenttip;
	}

}
