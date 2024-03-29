[ISSUES]: https://github.com/PssbleTrngle/DataTrades/issues

[DOWNLOAD]: https://www.curseforge.com/minecraft/mc-mods/data-trades/files

[CURSEFORGE]: https://www.curseforge.com/minecraft/mc-mods/data-trades

[MODRINTH]: https://modrinth.com/mod/data-trades

# Data Trades <!-- modrinth_exclude.start --> <img src="https://raw.githubusercontent.com/PssbleTrngle/DataTrades/1.19/common/src/main/resources/assets/data_trades/icon.svg" align="right" height="128" />

[![Release](https://img.shields.io/github/v/release/PssbleTrngle/DataTrades?label=Version&sort=semver)][DOWNLOAD]
[![Downloads](https://cf.way2muchnoise.eu/full_890866_downloads.svg)][CURSEFORGE]
[![Version](https://cf.way2muchnoise.eu/versions/890866.svg)][DOWNLOAD]
[![Issues](https://img.shields.io/github/issues/PssbleTrngle/DataTrades?label=Issues)][ISSUES]
[![Modrinth](https://img.shields.io/modrinth/dt/nm1MuVrD?color=green&logo=modrinth&logoColor=green)][MODRINTH]

<!-- modrinth_exclude.end -->

This mod adds the ability for players, serverowners or modpack creators to overwrite villager trades using datapacks.
The amount of trades per level and the actual items traded can be customized using json files.
A command for development purposes called `/villagers refresh` will manually trigger a re-asign of trades to all loaded villagers.

<!-- modrinth_exclude.start -->

* [Folder Structure](#folder-structure)
* [Examples](#examples)
* [JSON Schema](#json-schema)
    + [*Profession*](#profession)
    + [*TradeLevel*](#tradelevel)
    + [*Trade*](#trade)
    + [*TradeIngredient*](#tradeingredient)
* [Advanced Usage](#advanced-usage)

<!-- modrinth_exclude.end -->
## Folder Structure

Using a datapack, the trades of a specific villager profession can be overwritten by creating a JSON file following this
pattern:

```
data/[namespace]/villager/professions/[path].json
```

`[namespace]` and `[path]` should be overwritten by the specific villager professions ID.
For example, to create trades for the `minecraft:fletcher` profession,
a file under `data/minecraft/villager/professions/fletcher.json` should be created.
It's content should follow the [*Profession*](#profession) Schema

Similar, the wandering trader can be overwritten by adding a file at
```
data/minecraft/villager/traders/wandering.json
```
following the [*Trader*](#trader) Schema

## Examples

Datapacks with examples can be found [here](https://github.com/PssbleTrngle/DataTrades/tree/1.19/datapacks).

## JSON Schema

The JSON file should contain an object with these properties:

### *Profession*

| Property | Type                  | Default | Description                |
|----------|-----------------------|---------|----------------------------|
| levels   | *map<int,TradeLevel>* | `{}`    | *see below*                |

The `levels` object takes the form of a map with numeric keys and object values.
The keys represent the actual villagers level with *1* being *Novice* until *5* being *Master*.

### *Trader*

| Property | Type         | Default | Description    |
|----------|--------------|---------|----------------|
| generic  | *TradeLevel* | `null`  | generic trades |
| rare     | *TradeLevel* | `null`  | rare trades    |

In vanilla minecraft, each wandering trader chooses 5 generic and 1 rare trades once it spawns.
Both the trades themselves and their count can be overwritten using the same format as the one used for a villager's level.

### *TradeLevel*

| Property | Type                                                                                  | Default | Description                                                   |
|----------|---------------------------------------------------------------------------------------|---------|---------------------------------------------------------------|
| take     | *[NumberProvider](https://minecraft.wiki/w/Loot_table#Number_provider)* | `null`  | amount of trades added by this level                          |
| trades   | (*Trade* \| *string*)[]                                                               | `[]`    | list of trades from which the taken ones are randomly choosen |

### *Trade*

| Property        | Type                | Default | Description                                |
|-----------------|---------------------|---------|--------------------------------------------|
| disabled        | *boolean*           | `false` | disabled trades will be ignored            |
| sells           | *TradeIngredient*   | `[]`    | item sold to the player                    |
| wants           | *TradeIngredient*[] | `[]`    | maximum of two items the player has to pay |
| uses            | *int*               | `0`     |                                            |
| maxUses         | *int*               | `10`    |                                            |
| xp              | *int*               | `1`     |                                            |
| priceMultiplier | *float*             | `2.0`   |                                            |
| demand          | *int*               | `0`     |                                            |

### *TradeIngredient*

Extension of the vanilla `Ingredient` with a few extra properties.
They can take the form of an item or tag and specify all properties of the vanilla Ingredient JSON.
Since Villager trades are `ItemStack` based that does not mean that a player can pay a `#minecraft:stone_tool_materials`
with any stone type, but only the one that is randomly selected from the tag each time the trade is used.

| Property  | Type                                                                | Default |
|-----------|---------------------------------------------------------------------|---------|
| functions | *[LootFunction](https://minecraft.wiki/w/Item_modifier)*[] | `[]`    |
| count     | *int*                                                               | `null`  |

The `levels` object takes the form of a map with numeric keys and object values.
The keys represent the actual villagers level with *1* being *Novice* until *5* being *Master*.

## Advanced Usage

Instead of a `Trade` object, the TradeLevel `trades` property can also contain *string* IDs referencing an external
trade.
These trades can be saved under the path below to organize & reuse them.

```
data/[namespace]/villager/trades/[path].json
```
