This project has to do with Web "databases," and consists of two parts. In Part 1, you will implement the Web database classification algorithm that is described in the ACM TOIS '03 "QProber" paper by Gravano, Ipeirotis, and Sahami. In Part 2, you will implement a simplified version of the content-summary extraction algorithm that we discussed in class.

Part 1: Web Database Classification

As Part 1 of this project, you will implement the QProber database classification algorithm that we discussed in class, which is described in the ACM TOIS '03 "QProber: A System for Automatic Classification of Hidden-Web Databases" paper by Gravano, Ipeirotis, and Sahami. (This paper is part of the class reading list.) For your implementation, you should assume that the hierarchical categorization scheme that we will use consists of a root node with three children, "Computers," "Health," and "Sports," and that each of these three categories in turn has two leaf-level children, "Hardware" and "Programming" for "Computers," "Fitness" and "Diseases" for "Health," and "Basketball" and "Soccer" for "Sports." This 2-level categorization scheme is shown below:

Root
Computers
Hardware
Programming
Health
Fitness
Diseases
Sports
Basketball
Soccer
Your program should implement the query-probing classification algorithm described in Figure 4 of the QProber paper. You should ignore (i.e., not use) the "confusion matrix adjustment" step. Also, you don't need to do any "training" of your system. Instead, we are providing you with the queries that you should use at each internal node of the categorization scheme. (Just for your information, these queries were derived automatically using the Support Vector Machines (SVM) version of QProber.) You can get the queries by clicking in each category above. For example, the line "Computers avi file" in the file associated with the "Root" category indicates that documents containing both the words "avi" and "file" should be classified under "Computers." Therefore, the number of documents matching query [file](avi.md) in a database should be used towards the computation of the coverage of the database for the "Computers" category.

To derive the final classification of a database according to the Figure 4 algorithm, your program should receive as input:

The URL of the database to be classified (e.g., diabetes.org). You can assume that you will always get "http" URLs (e.g., not "https" or "ftp") and you should omit specifying "http" with the URL (i.e., your input should be diabetes.org and not http://diabetes.org).
The specificity and coverage thresholds. The specificity threshold  tes is a real number such that 0<= tes<=1, while the coverage threshold tec is an integer such that tec>=1.
You will use the Bing Search API. As you know from Project 1, this is Bing's open search Web services platform. The Bing Search API uses RPC-style operations over HTTP GET or POST requests with parameters encoded into the request URL. To use the API, and if you haven't done so already, you will have to sign up for an account to receive a "Bing Application Id," following the instructions at http://bing.com/developers. You will use your BING\_APP\_ID and QUERY as parameters to encode a request URL. When requested from a Web browser, or from inside a program, this URL will return an XML/JSON document with the query results. Please refer to the Bing API documentation at http://msdn.microsoft.com/en-us/library/dd251056.aspx for details on the URL syntax and the XML/JSON schema.

In your project, rather than querying each database directly, you will use the Bing API to avoid writing database-specific "wrappers." Specifically, you will use the "Site Restricted Search" feature of the Bing API, so that each query that you send to Bing matches Web pages only from the desired database (or Web site). For example, if you wanted to issue the QUERY [file](avi.md) to the diabetes.org database, you should encode the request URL using your BING\_APP\_ID and the QUERY, and add the string 'site:diabetes.org' to the query. The number of matches reported by Bing for this query indicates the number of documents in the diabetes.org Web site that match both the words "avi" and "file".

For testing, etc. you might want to save/cache locally the results that you get from Bing so that you only send a query to Bing if you haven't issued the exact same query before. This caching is not required, but it would help with the efficiency of your testing process.

Test Cases

So you can test your program, here are some Web sites and their associated classification in the above categories, as of 10/15/2011 and for tes=0.6 and tec=100. The first four cases are examples of good classification decisions by our algorithm, while the last one is not so good.

java.sun.com: Root/Computers/Programming
yahoo.com: Root
diabetes.org: Root/Health/Diseases
fifa.com: Root/Sports/Soccer
hardwarecentral.com:  Root/Computers

Note that the classification of a database will vary according to the threshold values that you choose, and that it is perfectly possible to classify a database under more than one category. Also, since these results were computed against the live version of Bing, which gets updated continuously, these results might of course change over time.

Part 2: Metasearching over Web Databases

As Part 2 of this project, you will write a program to build simple content summaries of Web databases, where a content summary of a database includes a list of words together with their (estimated) document frequency. As we discussed in class, content summaries are used during the database selection step of the metasearching process, to decide what databases to contact to execute a query, and what databases to ignore. You do not have to implement a database selection module, but rather just a program that will produce a content summary for a database, as described below.  (If you are interested in reading some more about all this, you can refer to the ACM TOIS '08 "Classification-Aware Hidden-Web Text Database Selection" paper by Ipeirotis and Gravano. Reading this paper is strictly optional, and you should not follow the more sophisticated techniques described there to construct content summaries, but rather just follow the procedure below.)

Part 2a: Document Sampling

You will construct the content summary of a database based on a small "sample" of the pages or documents in the database. You will extract the document sample of a database D  (e.g., diabetes.org) while you classify it with the procedure of Part 1, as follows:

For each category node C (e.g., "Root") that you "visit" while you classify database D (using the Part 1 procedure):
For each query q associated with this node (e.g., [file sites=diabetes.org](avi.md))  retrieve the top-4 pages returned by Bing for the query. (These are all pages in database D.)
The document sample associated with a category node C and a database D, to which we refer as sample-C-D, is the set of documents retrieved from D as above by queries associated with C, plus the set of documents retrieved from D by queries associated with subcategories of C that were visited during the classification of D. You should eliminate duplicate documents from the document samples. For example, suppose that we classify the diabetes.org database under the "Health" category. During classification, we then visited the "Root" category node and the "Health" category node (and no other category). Therefore, the document sample associated with the "Root" node and the diabetes.org database (i.e., sample-Root-diabetes.org) consists of the top-4 documents returned by Bing for each of the queries [sites=diabetes.org](cpu.md), [sites=diabetes.org](java.md), [sites=diabetes.org](module.md), ..., [windows sites=diabetes.org](pc.md), [sites=diabetes.org](acupuncture.md), ..., [league sites=diabetes.org](game.md), which are associated with the "Root" category, plus the top-4 documents returned by Bing for each of the queries [sites=diabetes.org](aids.md), ..., [sites=diabetes.org](aerobic.md), ..., [weight sites=diabetes.org](exercise.md), which are associated with the "Health" category.
Note: Please be nice to the Web sites when retrieving pages. For example, you might want to process (see below) each page that you retrieve before fetching the next page, so you space your requests. Alternatively, you can add a line to your program so that it waits for, say, 5 seconds in between page requests.

Part 2b: Content Summary Construction

After you obtained one document sample for each category node under which you classified a given database, you will build a "topic content summary" associated with each such sample. The topic content summary will contain a list of all the words that appear in the associated document sample, and their document frequency in the sample (i.e., the number of documents in the sample that contain each word). Your program should output each topic content summary to a text file, containing all the words in the sample -in dictionary order- together with their respective document frequencies. (Note that you do not have to implement the absolute-frequency computation strategy that we briefly discussed in class, but rather just compute the document frequency of a word simply as the number of documents in the sample that contain that word.)

For example, if the site diabetes.org was classified under category "Health" then your program should output two files: Root-diabetes.org.txt, with the topic content summary sample-Root-diabetes.org, and Health-diabetes.org.txt, with the topic content summary sample-Health-diabetes.org. The sample-Root-diabetes.org content summary is based on all the documents retrieved by "Root"-level queries plus all the documents retrieved by "Health"-level queries (see Part 2a), because "Health" is a subcategory of "Root." In contrast, the sample-Health-diabetes.org content summary is based only on all the documents retrieved by "Health"-level queries.

To extract the text of a page, you can use the command "lynx --dump", available on the CS machines. Any part of the text after the "References" line should be ignored. Also any text within brackets "[....]" should be ignored. Any character not in the English alphabet should be treated as a word separator, and the words are case-insensitive. Alternatively, you can directly use or adapt our Java script, available here. This script converts an HTML document to lowercase, treats any character not in the English alphabet as a word separator, and  then returns the set of  words that appear in the document.

Note: You do not have to output content summaries for level 2 categories (i.e., for leaf categories), as there are no additional queries performed at this level.