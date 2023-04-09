# Hexo Blog Manager
A simple UI for hexo blog management

[Chinese(Simplified) 简体中文](https://ajie.wiki/2023/04/09/TheHexoBlogManagerDOCS-zh-CN/)

## Download

**Download the HexoBlogManager.jar**

## Get started

This document helps you get started with the Hexo Blog Manager more quickly.

Features Implemented
--------------------

*   Markdown file management
    
    *   Display the name and priority of markdown files.
    *   Freely view and modify the detailed header information of markdown files.
    *   Quickly create new markdown files (and their corresponding folders).
    *   Delete markdown files (and their corresponding folders).
*   Blog generation and deployment
    
*   Automatic backup
    

> More features to be developed.

Usage
-----

## Running Hexo Blog Manager

*   For HexoBlogManager.jar

> To run a .jar file, you need to first install the Java Runtime Environment (JRE). If you do not have JRE installed, download and install it from the Oracle official website. Then, follow these steps to run the Hexo Blog Manager.jar file:

**You can directly double-click the jar file to open it.**

Or

1.  Open the directory containing the HexoBlogManager.jar file in the terminal.
2.  Enter the following command in the command line or terminal window: `java -jar HexoBlogManager.jar`
3.  Press Enter to start the application.

*   For Hexo Blog Manager.exe

**To run the .exe file, simply double-click the file to start the application.**

## Requirements

As it is aimed at most users, the operating system is Windows and does not support Linux and MAC.

Before using it, you should ensure that your hexo can successfully execute `hexo clean` `hexo g` `hexo d`. Otherwise, unexpected errors may occur due to some reasons.

## User Interface Introduction

This application has the following six buttons:

![PIC1](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-1.webp)

From top to bottom, from left to right, they are:

*   Markdown file management: Manage Markdown files. You can create, edit, rename, and delete Markdown files.
*   Blog generation and deployment: Clear,
*   Settings: You can change the blog path through this button.
*   Open the blog post directory: You can quickly find all articles through this button.
*   About the author: View the author information of the application.
*   Help documentation: View the help documentation of the application.

The following will introduce each part in detail.

## Settings

There are two settings in total:

*   Blog root directory
*   cwebp directory

![PIC8](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-8.webp)

Fill in the root directory of your hexo blog in the first input box.

Fill in the root directory of your cwebp in the second input box.

### What is cwebp?

cwebp is a tool that can compress image files into WebP files.

[Click here to download WebP for windows](https://storage.googleapis.com/downloads.webmproject.org/releases/webp/libwebp-1.3.0-windows-x64.zip)

This is an introduction document (no need to understand, can be skipped)

[https://developers.google.com/speed/webp/docs/cwebp](https://developers.google.com/speed/webp/docs/cwebp)

### Why do you need cwebp?

Compressing image files into WebP files can greatly compress images, save costs such as traffic, and improve user access speed, which can improve the user experience.

## Markdown file management

To manage Markdown files, click the "Markdown file management" button. In this page:

### Article Management Panel

Displays the name of the article files and their priority (sticky). The displayed priority is almost the same as the actual deployment. The format displayed is `article (priority)`. If some articles in themes that support `top: true` have a top project, then the top will be sorted first. The remaining articles that do not include `top: true` will be arranged in descending order according to the priority `sticky: num`, i.e., sorted from the highest to the lowest based on the sticky value. If some articles do not have `sticky: num`, they will be sorted last.

> In the example below, example.md has the `top: true` attribute, so it has the highest priority. HelloWorld.md has a `sticky: 999` attribute, with a value much higher than the rest of the sticky values, which are (6, 5, 4, 3, 2, 1, null), so it has a higher priority than the .md files with smaller or no sticky values. example2.md does not have the `sticky: num` attribute, so it has the lowest priority.

![PIC2](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-2.webp)

### Article Creation Panel

Click the "New Article" button to call up the creation panel.

![PIC3](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-3.webp)

*   `Title`: The title of the article, which is a required field.
*   `Category`: Select the type of article. Here, an article can only select one type. In the future, it may be changed to allow multiple types to be selected (but this seems to have no difference from tags).
*   `Tags`: Select the tags of the article. Hold down the Ctrl key and click the left mouse button to select multiple items.

> Note: Some themes have a maximum number of supported tags. If this number is exceeded, different themes will handle it differently.

*   `Priority`: That is sticky, which defines the priority of the article. If you check the Enable on the right, it will be enabled. Otherwise, this tag will not be used.
*   `Abstract`: That is excerpt, which defines the abstract of the article. If you check the Enable on the right, it will be enabled. Otherwise, this tag will not be used.
*   `Thumbnail`: Select the thumbnail of the article. If you check the Enable on the right, it will be enabled. Otherwise, this tag will not be used.
*   `Cover`: Select the cover of the article. If you check the Enable on the right, it will be enabled. Otherwise, this tag will not be used.

> It is worth noting that the absolute path writing method is used here. For example,

![PIC4](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-4.webp)

> If you have installed some plugins that change the path after generating the static website, such as abbrlink, etc., this function will not be available, please manually configure it in the subsequent attribute modification panel.

Click the submit button to create your article.

After creating it, a prompt box will pop up, and you can click the button inside to quickly access the file you created.

**Manage Your Tags**

Click the `Categories & tags management` button in the new panel to manage your categories and tags. Enter the information in the input box at the top and click `+` to add it, and select a project and click `-` to delete it.

![PIC6](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-6.webp)

### Article Deletion

Select an article and then click the "Delete Article" button to delete the article and its corresponding folder. If multiple articles are selected, only the first one will be deleted. To ensure safety, a prompt box will pop up after selecting the article, asking if you want to confirm the deletion.

### Refresh Button

If you manually modify a file's attribute values using an editor, you can simply click the refresh button to reload the content.

### Attribute Viewing and Editing Panel

Select an article and click the `Details` button to access the attribute values of the file.

![PIC5](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-5.webp)

You can freely add a `Add new` and delete key-value pairs with the `.md` file header. However, the key must be one of the following:

*   title
*   date
*   updated
*   categories
*   tags
*   sticky
*   keywords
*   description
*   thumbnail
*   cover
*   top
*   comments
*   excerpt
*   toc
*   mathjax

Otherwise, the key-value pair will not be added successfully.

The attribute viewing and editing panel also arranges the key-value pairs in this order.

After you have finished editing, click `Submit Changes` to apply these modifications to the file.

## Blog Generation and Deployment

To generate and deploy the blog, click the `Blog Generation and Deployment` button. On this page, you can perform the following actions:

*   Clear content, i.e. `hexo clean`
*   Generate and open a local server at [http://localhost:4000/](http://localhost:4000/), i.e. `hexo g && hexo s`
*   Generate and deploy to GitHub, i.e. `hexo g && hexo d`

![PIC7](https://raw.githubusercontent.com/BenChenEternity/Images/TheHexoBlogManagerDOCS-en-US-7.webp)

> After you open the server locally, it is best not to force the process to end through the task manager, as this may cause the background process that occupies port 4000 to not release. After clicking `Generate and open on localhost`, Hexo Blog Manager will forcibly end the current process that occupies port 4000 (if any).

## Open Blog File Directory

Click the "Folder" icon button to quickly access the `.md` files of your blog.

## About the Author

To view information about the author of the application, click the "About the Author" button, where you can report bugs to my email or visit my GitHub page.

## Help Document

To view the help document for the application, click the button with the "Help Document" icon.
