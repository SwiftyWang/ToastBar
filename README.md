Top Material Design Top Toast Bar.

Support in the application or in the window.

Toast style to use:

with container view:
```java
    public void onBottomToast(View view) {
        BottomToast.make((ViewGroup) findViewById(R.id.parent), context, "HELLO WORLD!!!!", 3000).show();
    }

    public void onTopToast(View view) {
        TopToast.make((ViewGroup) findViewById(R.id.parent), context, "HELLO WORLD!!!!", 3000).show();
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
