# Expenses Keeper App (android code challange)

The app has been developed in Android Studio Dolphin Canary build (2021.3.1 Canary 1) on Apple M1 laptop. Due to this fact, it uses alpha versions of Gradle and Android Gradle plugin. It has also been tested on latest production version of Android Studio on Ubuntu and it compiles and runs without any issue. Anyway, I don't recommend testing the app on Apple M1 (Apple Sillicon) android emulator as there is an issue with emulator camera.

### Features
- can be easily extended to different data source (rest api)
- support both invoices and receipts
- uses Dagger2 for DI, Room as DAO layer
- Possible to easily add new features like:
1. Automatic recognition of fields from document via MLKit
2. Photo rotation, crop and contrast improvements
3. Include VAT fields in records
4. Filters and sorting options for documents (paid/not paid documents, partner, invoice number etc.)