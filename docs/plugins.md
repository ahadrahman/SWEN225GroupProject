<h2>Plugins</h2>
<h4>Implementation</h4>
<p>The levels as plguins extention is implemented using two classes 
from the Java standard library; ZipFile and URLClassLoader. ZipFile is used to
read the contents of the .zip folder while URLClassLoader is used to load the 
.class files.</p>

<p>The Level-k.zip folder contains Level-k.json which describes the layout of the level, the
.class file for each tile and item in the level, and .png files for each tile/item. 
Tiles and items inlcuded in the first level are considered standard for all levels so are
excluded from the plugin. When a level is loaded each file in the zip folder is read using
ZipFile.getInputStream(ZipEntry). The input stream is copied to an non-compressed file to be
read later. Persistence.LoadJSON attempts to load the class from the Maze package using 
Class.forName(). The name of the class at a specific co-ordinate is read from Level-k.json. 
If a ClassNotFoundException exception is caught then Persistence.LoadJSON uses the URLClassLoader 
to load the class from the folder of non-compressed .class files made from the zip folder. 
A similar process is used to load the .png files. Maze.Item and Maze.Tile have a getImage() 
method which returns the associated image. getImage() tries to load the image from the 
resources folder but an IOException is caught the image is loaded from the folder of 
non-compressed images.</p>

<h4>Proxy Design Pattern</h4>

<p>Our implementation of levels as plugins is an example of the proxy design pattern.
The abstract classes Maze.Tile and Maze.Item act as proxies to 'real' tiles and items that
appear in the game. The methods in other classes work through the tile or item proxy. As the 
methods work through the proxy they are not aware of the differences between the 'real' 
implementations. This is important as it allows new differences to be introduced without affecting
the methods in other classes.</p>

<p>In our game each Tile in the level must extend Maze.Tile, an abstract class. Similarly, items must 
extend Maze.Item. Enemies with tick based behaviour need to extend the abstract class
Maze.Enemy. This ensures that methods in the standard classes will work with any 
implemention the user writes. For example, our doMove() method in Application.Main
calls interact() on each item placed on the tile the player moves to. This is possible
because all items must extend Maze.Item and therefore implement interact(). The doMove()
method is not aware of what interact() will do. This is important because it allows level
creators to define their own interactions (i.e. introduce new diffesrc/Utilityrences between objects) 
without affecting the code in the standard classes.</p>

<h4>Making Your Own Level</h4>
<p>If you wanted to make your own level it would be a simple process. <ol><li>Decide on what
new tiles and items you want to have in your level.</li><li>Write classes describing those tiles
and items that extend Maze.Tile or Maze.Item.</li><li>Create images for each tile and item. The images
should be in the format "CLASSNAME.png"</li><li>Create a .json file clalled "Level-k.json". 
It will describe the potition of every item and tile in the level.</li><li>Finally, simply place all your 
.class files, .png files, and .json file into a zipped folder called "Level-k.zip".</li></p>