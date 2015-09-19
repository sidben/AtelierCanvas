# Atelier Canvas Design Guidelines

**Main goal:** Allow players to add custom paintings to the game.

Other goals:

- Players should be able to add the images they want, without relying on pre-made packs;
- There must be a way for players to share their paintings in a 'pack' (Base64 encode?);
- In SMP, paintings must be visible to players even if they don't have the image file (like maps);
- Paintings should have a NBT value to indicate if it's the original or a copy (like writen books);
- There must be a way to uniquely identify a paintings, so the mod can keep track of the original and avoid re-adding an existing painting;
- Paintings should also keep track of: creator (player), creation date, last update date;
- There can only be one painting with the tag 'original' per world, unless manually spawned with commands;
- Custom paintings could be added to the terrain generation (inside villages houses, strongholds, dungeon chests);



## Parameters

- Whitelisted URLs (hard-coded): Websites the mod can check to download images;
- Whitelisted extensions (hard-coded): File extensions the mod will accept;
- Max file size (Kbytes): Maximum file size the mod will download;
- Max images per user: SMP-only config, allows the server owner to limit how many custom paintings each player can 'submit' or create;



## Safety concerns

The player must be able to 'install' new paintings from a URL. To prevent download of malicious code,
the mod can only download from hard-coded, **whitelisted URLs**. Initially, only imgur.com will be whitelisted.

Server owners (/op) should be able to view all custom paintings in the server, including player submitted ones.
They should be able to disable or remove any paintings. They should be able to ban a player from submiting custom paintings.



## Blocks and items

- Easel: Used to create or copy paintings.
- Painting Pallete: Used with the easel. Stack to 64 (?). Has 1024 uses, each pixel cosumes one tool use. 
A painting copy would consume 256 uses (16x16 pixels). Crafted with 1 wooden plank and dyes representing RGB + CMYK and maybe a feather.
- Blank Canvas: Used to copy or create paintings.
- Custom Paintings: Actual painting, always have the same art.
- Mysterious looking painting: An item that, when right-clicked, gives the player a random custom painting. 
Could be used as a way for the player in SSP to unlock new paintings without breaking immersion. The given
item would have a small, but not rare chance of being the original work (10%?).



## Mod mechanics

Each blank canvas can create one 16x16 painting. Bigger paintings requires multiple canvas.

A player can use the Easel to copy any painting they know. (?) Codewise, a player is considered to
know all vanilla paintings and all paintings they have installed (subject to change).

MAYBE: The easel should have 2 slots, one for the current pallete (stack 1) and one for the
pallete 'stock' (stack 64). The current pallete loses durability with each action. Once it's empty, 
1 pallete from the stack moves to the active slot automatically.

MAYBE: Easel GUI allow a player to select multiple brushes: 1x1px, 2x2px, 4px round, bucket (fill all)

MAYBE: The easel record the last 5 action, allowing the player to undo mistakes.

Once a painting is signed, it can't be changed anymore (like writen books). It's possible to make a copy of 
the painting and edit the copy (?).

NOTE: add a way to make public message boards, where all copies of a painting would link to the original.
Maybe a player can SIGN or MAKE PUBLIC, Public painting can be edited, but only by the author. But with that
feature, what is the reason to sign a painting and making it read-only?