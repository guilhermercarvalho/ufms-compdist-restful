#include <stdio.h>


int main(int argc, char *argv[], char *envp[])
{
	int i = 0;
	
	printf("<!DOCTYPE html>\n\
<html lang=\"en\">\n\
<head>\n\
  <meta charset=\"UTF-8\">\n\
  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n\
  <title>Print_ENV</title>\n\
</head>\n\
<body>\n\
<h1>Variáveis de Ambiente</h1>\
<ul>\n");  

	while (envp[i])
		printf("<li>%s</li>\n", envp[i++]);
	
printf("</ul>\n\
</body>\n\
</html>");
}
