# Atelier Canvas Design Guidelines

**Main goal:** Allow players to add custom paintings to the game.

Other goals:

- Players should be able to add the images they want, without relying on pre-made packs;
- There must be a way for players to share their paintings 'pack';
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

- Easel: Used to create custom paintings or select paintings.
- Blank Canvas:
- Custom Paintings: Actual painting, always have the same art.
- Mysterious looking painting: An item that, when right-clicked, gives the player a random custom painting. 
Could be used as a way for the player in SSP to unlock new paintings without breaking immersion. The given
item would have a small, but not rare chance of being the original work (10%?).



## Mod mechanics

Each blank canvas can create one 16x16 painting. Bigger paintings requires multiple canvas.

A player can use the Easel to copy any painting they know. (?) Codewise, a player is considered to
know all vanilla paintings and all paintings they have installed (subject to change).