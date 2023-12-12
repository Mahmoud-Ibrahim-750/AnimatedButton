# AnimatedButton Library

[![](https://jitpack.io/v/Mahmoud-Ibrahim-750/AnimatedButton.svg)](https://jitpack.io/#Mahmoud-Ibrahim-750/AnimatedButton)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)


AnimatedButton is an Android library that allows you to create animated loading buttons with ease. When the button is pressed, it smoothly shrinks to a smaller size, displaying a progress bar indicating the loading action. Once the loading is complete, the button dynamically shows either a success or failure icon based on your implementation.

## Demo

Here's a visual representation of AnimatedButton in action:

![AnimatedButton Demo](animated_button_sample.gif)

In this example, the AnimatedButton demonstrates its loading animation, success state, and failure state.

To explore and run this example, you can check out the [app module](/app) in the repository.

## Installation

You can easily integrate AnimatedButton into your Android project by adding the following dependency in your app's `build.gradle` file.

### Gradle

Step 1. Add the JitPack repository to your build file:

```gradle
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency:

```gradle
dependencies {
    implementation 'com.github.Mahmoud-Ibrahim-750:AnimatedButton:1.0.4'
}
```


For the latest version, check the [releases page](https://github.com/Mahmoud-Ibrahim-750/AnimatedButton/releases), or the [JitPack page](https://jitpack.io/#Mahmoud-Ibrahim-750/AnimatedButton).

## Usage Example

To use AnimatedButton in your project, follow these simple steps:

1. Add the `AnimatedButton` widget to your XML layout file.

```xml
<com.mis.animatedbutton.AnimatedButton
    android:id="@+id/animated_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:doneIcon="@drawable/ic_default_done"
    app:doneIconTintColor="@color/green"
    app:errorIcon="@drawable/ic_default_error"
    app:errorIconTintColor="@color/red"
    app:indicatorColor="@color/white"
    app:text="Login"
    app:textColor="@color/white"
    app:textSize="20sp"
    app:textStyle="bold" />
```

2. In your activity or fragment, find the button by its ID (or use view binding if you prefer) and set a click listener.

```kotlin
animatedButton = findViewById<AnimatedButton>(R.id.animatedButton)

// define when the button should stop loading and whether to show success or failure
animatedButton.setOnClickListener {
    // e.g., stop loading after 2 seconds
    Handler(Looper.getMainLooper()).postDelayed({
        // indicate action is done
        animatedButton.animateLoadingToDoneState()
        // or action has failed
//        animatedButton.animateLoadingToErrorState()
    }, 2000)
}
```

3. Customize the appearance and behavior as needed using XML attributes or programmatically.

## Attributes

- `backgroundColor` - Background color of the AnimatedButton.
- `backgroundResource` - Background resource for the AnimatedButton.
- `indicatorColor` - Color of the loading indicator.
- `doneIconTintColor` - Tint color for the success icon.
- `errorIconTintColor` - Tint color for the failure icon.
- `text` - Text to display on the button.
- `textSize` - Size of the text on the button.
- `textStyle` - Style of the text on the button. Options: `normal`, `bold`, `italic`.
- `textColor` - Color of the text on the button.
- `errorIcon` - Drawable resource for the error icon.
- `doneIcon` - Drawable resource for the success icon.


For more details and customization options, refer to the [documentation](link_to_documentation).

## Credits

This library is inspired by and based on the [MyLoadingButton](https://github.com/yatindeokar/MyLoadingButton), the project that provided the foundation for AnimatedButton. While the original library is no longer actively maintained, we appreciate the contributions of the author.

## License

This library is licensed under the [MIT License](LICENSE).

---
