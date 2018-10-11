# Android Development

## Contributing

We will be working with a `master` and `dev` branch.

*   `master` is the main branch with finished working source code of the project. 
*   `dev` will be a main development branch to branch off and build features to test together before merging to the master branch 

![alt text](https://nvie.com/img/main-branches@2x.png)

In order to push to `dev` you must first push a new branch with your changes to GitHub. The automated tests will run within a minute or two and if they pass you will be able to push your commits to `dev`.

#### The recommended workflow for making new features/changes is:
  1. Checkout `dev` and pull the latest changes from GitHub
    - `git checkout dev`
    - `git pull origin dev`
  2. In order to branch off of the `dev` branch and create a new feature
   - `git checkout -b myFeature dev`
  3. Make your changes and commits (There are many commands/options to accomplish this, these are just a few)
     - See what changes you made:
      - `git status`  - Shows the modified files
      - `git diff [<file-name>]` - Shows the modified lines
     - Stage the changes (Choose what changes will be part of the commit)
      - `git add <file-name>` - Add a specific file or pattern
      - `git add -p` - Interactively choose chunks of code to add (My favorite)
      - `git add .` - Add all changes (Use sparingly)
     - Make the commit
      - `git commit -m "<commit-message>"` - When the commit message is short
      - `git commit` - Will open a text editor to enter longer commit messages
  4. Get any new changes from `dev` (It is a good idea to do this throughout the development of your feature as well.)
    - `git pull origin dev`
  5. You may also pull changes from any specific branch
    - `git pull origin dev`
  6. Push your branch to GitHub
    - `git push origin <branch-name>`
  7. Make sure to create a pull request on GitHub.
    - `GitHub` > `Pull requests` >  `New pull request` > `compare: <branch-name>` > `Create pull request` > Fill in the description > `Create pull request`
  
 ## Android Studio Formatting
 
 To keep the code easy to read we will be using Android Studio's built in formatting tool. Make sure to format your code before pushing to GitHub, To format your code you will need to use the following commands in Android Studio:
 
 *  For Windows users use:
    * <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>L</kbd>

 *  For Mac users use:
    * <kbd>⌘ Command</kbd> + <kbd>⌥ Option</kbd> + <kbd>L</kbd>
