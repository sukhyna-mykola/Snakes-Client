# Snakes-Client
###Для запуску проекту потрібно:
1. Завантажити zip або клонувати проект за допомогою git; 
2. Щоб відкрити проект, на вашій машині повинно бути встановлено JDK та середовище розробки Android Studio(при необхідності потрібно завантажити 
додаткові копомпоненти в SDK Manager);
3. Запустити емулятор або підключити реальний android девайс;
4. Запустити проект, натиснувши кнопку "Run";

###Щоб згенерувати apk потрібно:
1. Завантажити zip або клонувати проект за допомогою git; 
2. Перейти в папку з проектом;
3. Збудуватии образ проекту командою `docker build -t my-image . `;
4. Запустити контейнер командою `docker run -t -v $(pwd)/app:/project/my-image:latest ./gradlew clean assembleRelease`;
