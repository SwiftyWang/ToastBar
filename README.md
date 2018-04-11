Material Design Top/Bottom Toast Bar.


<h3>Min SDK >= 14</h3>
<h3>Support Android 7.1.1 and 8</h3>

<h2>How to implement</h2>

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
            compile 'com.github.SwiftyWang:TopToastBar:1.1.6'
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

<h2>ToDo list:</h2>
- [ ] add rignt button in the toast.
- [ ] support customer layout.
- [ ] add customer animation support.
