<div align="center">

# GamepadEx

Simple bindings for controller buttons

</div>

GamepadEx is an ergonomic package for FTC teams to improve their experience of programming with Gamepads.

The package allows for the binding ( assigning ) of actions to buttons BEFORE
the main loop. This drastically reduces the clutter of the main loop, which
can become very difficult to debug in long OpModes.

## Usage

```java

import friends.GamepadEx;
import static friends.GamepadButton.*;

@TeleOp
public void runOpMode() {
    // ...

    // This will be used instead of gamepad1
    GamepadEx primary = new GamepadEx(gamepad1);

    // XBOX buttons are also accepted
    // primary.bind(A, ...);

    // When CROSS is pressed, run the code below
    primary.bind(CROSS, (gamepad, reader) -> {
        telemetry.addLine("Button A is pressed!");
    };

    // You can also use bindN
    // When CROSS isn't pressed, run the code below
    primary.bindN(CROSS, (gamepad, reader) -> {
        telemetry.addLine("Button A is NOT pressed!");
    });

    // Edge detectors are also accessable through the reader
    primary.bind(B, (gamepad, reader) -> {
        // Will only run once when the button is pressed
        if(!reader.justPressed()) return;
        gamepad.setLedColor(255, 0, 0, -1);
    });

    // ...

    while(opModeIsActive()) {
        // Needed to keep button state correct
        primary.update(gamepad1);
    }
}
```

## Installation

Download the repository, and place the `friends` folder into the root
of your `TeamCode/java` directory. This might vary depending on your
project layout, but in general it should be placed next to the folder
with your actual robot code in it.

This will allow you to use `import friends.*` in your code.

Statically importing the GamepadButton enum is also recommended, which
prevents you from having to qualify the enum member with the entire enum name.

```java
import static friends.GamepadButton.*;
```
