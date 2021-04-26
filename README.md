# Twitter Video Downloader

This is an Android app to download twitter videos by the Twitter API.

It's built in Kotlin with the latest development tools: Jetpack Compose, ViewModel and Retrofit.

To run the app you need a Twitter API bearer token to call the API. This is how you can get one and make the app use it.

## Get Bearer token for Twitter API

You need a bearer token to make requests to the Twitter API. Here is how you can get one.

1. Login to your Twitter account on [developer.twitter.com](developer.twitter.com).
1. Navigate to the [Twitter App dashboard](https://developer.twitter.com/en/portal/projects-and-apps)
1. If you have an existing app, go to the next step. If not, to create an app, you will be first asked to create a
   project. You can give the app the name you want. When asked the access level required for your app, select Read Only,
   it will be enough for the script to work.
1. Open the app and navigate to the "keys and tokens" page.
1. You'll find the "bearer token" on this page.

## Configure the app to use the token

This script reads the Twitter bearer token from .env file.

1. Create a file named "secret.properties" at the root of the project.
2. Write the token as below:

```properties
BEARER_TOKEN="<your token here>"
```

## License

MIT License

Copyright (c) 2021 Bakoura Ibrahima

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
