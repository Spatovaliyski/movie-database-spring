## Documentation

This is the praktikum-2020 Boostrap + Ajax Exam 
Live preview: https://spatovaliyski.github.io/praktikum-exam/

### Running the app locally

Simply clone and open index.html, no dev dependencies are needed here;
To re-compile the asset files (Scripts & Styles), install Node 11 and run "npm install" in the folder first. node_modules will be generated, after that simply run "gulp" to compile all assets :)
---
Препоръчително е да се прегледа проекта на резолюция над 1366x768 за пълната му функционалност. Оптимизарн е за малки устройства също. Главната идея над Буутстрап тук е да не бъдем толкова зависими от него, но въпреки това да се използват някои важни елементи като грида, бутони, отстъп, шрифтове и т.н.
Буутстрап е инсталрин с Node, за което е локално зареден; jQuery зарежда под CDN;
Целия ръчно написан CSS код е в следният файл: https://github.com/Spatovaliyski/praktikum-exam/blob/master/src/scss/_movie.scss, където е използван SCSS като препроцесор и Gulp за компилация