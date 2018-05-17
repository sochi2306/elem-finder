elem-finder
===========

Elem Finder is a simple command line tool that finds elements in a given HTML document that are similar to the target element provided in the original HTML document.

An element is treated as similar if one of the following is true:
* The id of the current element exactly matches the id of the target element
* The text of the current element contains the text of the target element
* The class of the current element exactly matches the class of the target element
* The title of the current element exactly matches the title of the target element

The priority of each of the method is in accordance with how it is listed above. If one of the method's conditions are met, the other methods are not checked, so that the method with the highest priority wins.

## Instructions
1. Clone the git repository by running ```git clone https://github.com/sochi2306/elem-finder.git```
1. Navigate to the folder containing the executable jar (can be found in ./elem-finder/target/elem-finder-1.0.jar)
2. Run ```java -jar test-task-1.0.jar <origin_file_path> <other_sample_file_path> <target_element_id>```

### Example
Note that you can find the sample HTML pages in ./target/test-classes/sample/
```
java -jar elem-finder-1.0.jar test-classes/sample/sample-0-origin.html test-classes/sample/sample-1-evil-gemini.html make-everything-ok-button
INFO  Found [1] similar element(s):
INFO  elementDesc = [tag = 'a'; class = 'btn btn-success']; elementPath = [tag = 'html'] > [tag = 'body'] > [tag = 'div'; id = 'wrapper'] > [tag = 'div'; id = 'page-wrapper'] > [tag = 'div'; class = 'row'] > [tag = 'div'; class = 'col-lg-8'] > [tag = 'div'; class = 'panel panel-default'] > [tag = 'div'; class = 'panel-body']
```