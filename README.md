# Parliament_Browser_6_4
The programme runs under java version 11.
The program can run the following functions:
1: Download the parliamentary file from the official website of the German Federation
2: Store information about the parliamentarians and their photos in mongoDB
3. access the parliament web page
4. If you want to download the parliamentary documents please run DownloadTask
5. If you want to import information about members of parliament and their photos into a database,
6. run SyncDBTask.
7. If you want to access the webpage, please run Launch and enter locaolhost 8080 in your browser. 
8. For other web operations, please refer to the user manual.
9. About the pdf file download operation:
   Please ensure that your computer has installed Texlive and contains pdfTex package!
   Inadequate programme and errors:
   In the web page for pdf file download operation, due to the existence of some special symbols
10. in the file, resulting in part of the latex file can not be resolved, in the code to add StringUtils 
11. file has been improved, some of the symbols can be in the StringUtils after the operation of 
12. the smooth resolution, but there is still a small part of the file can not be resolved by the
13. special symbols resulting in pdf file output when the occurrence of Error.


