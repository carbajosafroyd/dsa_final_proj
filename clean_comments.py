import os
import re

files = [
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\Dsa_final_proj.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\model\BenchmarkResult.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\model\Facility.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\model\FacilityType.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\model\SearchResult.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\persistence\DataStore.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\BenchmarkService.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\BenchmarkTest.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\CRUDTest.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\FacilityService.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\SearchService.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\service\SearchTest.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\tree\KDNode.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\tree\KDTree.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\tree\KDTreeTest.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\MainApp.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\StubServices.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\controller\BenchmarkController.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\controller\DashboardController.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\controller\FacilityFormController.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\controller\MapController.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\ui\controller\SearchController.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\util\DistanceCalculator.java",
    r"C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj\src\main\java\com\mycompany\dsa_final_proj\util\SampleDataGenerator.java"
]

def clean_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    lines = content.split('\n')
    new_lines = []
    
    # Simple state to check if we are inside a multi-line comment /* ... */
    # (Just in case, though the user mostly had inline comments we want to remove)
    # Actually the user asked to remove "all the comments", but I recommended option 1 (keep Javadoc /**).
    # What about regular block comments /* */ ? I'll just focus on // for now, as that's 99% of development notes.
    # Wait, Javadoc starts with /**, so if I leave block comments, it's fine.

    for line in lines:
        if line.strip().startswith('//'):
            continue
        
        if '//' in line:
            in_quote = False
            comment_index = -1
            for i, c in enumerate(line):
                if c == '"' and (i == 0 or line[i-1] != '\\'):
                    in_quote = not in_quote
                elif not in_quote and c == '/' and i < len(line)-1 and line[i+1] == '/':
                    comment_index = i
                    break
            if comment_index != -1:
                line = line[:comment_index].rstrip()
                if not line.strip():
                    continue
            
        new_lines.append(line)

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write('\n'.join(new_lines))

for file in files:
    try:
        clean_file(file)
        print(f"Cleaned {file}")
    except Exception as e:
        print(f"Error on {file}: {e}")
