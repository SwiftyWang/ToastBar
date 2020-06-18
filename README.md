Material Design Top/Bottom Toast Bar.


## Min SDK >= 14
## Support Android 10

### How to implement

<img src='https://github.com/SwiftyWang/ToastBar/blob/master/sample/2016_11_25_10_11_11_10_37_12.gif' height='600'/>

Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Step 2. Add the dependency
```gradle
    dependencies {
            implementation 'com.github.SwiftyWang:TopToastBar:1.1.7'
	}
```

Support in the application or in the window.

Toast style to use:

with container view:
```java
    public void onBottomToast(View view) {
        BottomToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopToast(View view) {
        TopToast.make((ViewGroup) findViewById(R.id.parent), "HELLO WORLD!!!!", 3000).show();
    }
```

without container view(attach as overlay)
```java
    public void onBottomToast(View view) {
        BottomToast.make(context, "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopToast(View view) {
        TopToast.make(context, "HELLO WORLD!!!!", 3000).show();
    }
```

all public apis:

```java
setAnimationInterpolator(Interpolator enterInterpolator, Interpolator exitInterpolator);

setPosition(Position position);

setText(String text);

setTime(long time);

show(long delay);

show();

setBackground();

setTextColor();
```

### ToDo List
- [ ] add rignt button in the toast.
- [ ] support customer layout.
- [ ] add customer animation support.
