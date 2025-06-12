package org.friends;

import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class GamepadEx {
    private final Map<GamepadButton, BiConsumer<Gamepad, ButtonReader>> bindingsPos =
            new HashMap<>();
    private final Map<GamepadButton, BiConsumer<Gamepad, ButtonReader>> bindingsNeg =
            new HashMap<>();
    private final Map<GamepadButton, ButtonReader> buttonReaders = new HashMap<>();

    private Gamepad gamepad;

    public GamepadEx(Gamepad gp) {
        gamepad = gp;
    }

    public void bindPos(GamepadButton btn, BiConsumer<Gamepad, ButtonReader> callback) {
        if(bindingsPos.containsKey(btn)) return;
        if(!buttonReaders.containsKey(btn)) {
            ButtonReader reader = new ButtonReader(this, btn);
            buttonReaders.put(btn, reader);
        }
        bindingsPos.put(btn, callback);
    }

    public void bindNeg(GamepadButton btn, BiConsumer<Gamepad, ButtonReader> callback) {
        if(bindingsNeg.containsKey(btn)) return;
        if(!buttonReaders.containsKey(btn)) {
            ButtonReader reader = new ButtonReader(this, btn);
            buttonReaders.put(btn, reader);
        }
        bindingsNeg.put(btn, callback);
    }

    /// Creates bindings for positive and negative bindings
    public void bind(
            GamepadButton btn,
            BiConsumer<Gamepad, ButtonReader> pos,
            BiConsumer<Gamepad, ButtonReader> neg) {
        bindPos(btn, pos);
        bindNeg(btn, neg);
    }


    /// This updates the entire gamepad
    /// It loops through the bindings and alternate bindings, and accepts the BiConsumers
    /// Also updates the button readers
    public void update(Gamepad gp) {
        gamepad = gp;

        // Update all button readers
        for(Map.Entry<GamepadButton, ButtonReader> entry : buttonReaders.entrySet()) {
            ButtonReader reader = entry.getValue();
            reader.read();
        }

        for (Map.Entry<GamepadButton, BiConsumer<Gamepad, ButtonReader>> entry :
                bindingsPos.entrySet()) {
            if (get(entry.getKey())) {
                ButtonReader reader = buttonReaders.get(entry.getKey());
                entry.getValue().accept(gamepad, reader);
            }
        }

        for (Map.Entry<GamepadButton, BiConsumer<Gamepad, ButtonReader>> entry :
                bindingsNeg.entrySet()) {
            if (!get(entry.getKey())) {
                ButtonReader reader = buttonReaders.get(entry.getKey());
                entry.getValue().accept(gamepad, reader);
            }
        }
    }

    /// Returns the state of the given button, regardless of controller type
    public boolean get(GamepadButton btn) {
        switch (btn) {
            case A:
            case CROSS:
                return gamepad.a;
            case B:
            case CIRCLE:
                return gamepad.b;
            case X:
            case SQUARE:
                return gamepad.x;
            case Y:
            case TRIANGLE:
                return gamepad.y;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger > 0.1;
            case LEFT_TRIGGER:
                return gamepad.left_trigger > 0.1;
            case DPAD_LEFT:
                return gamepad.dpad_left;
            case DPAD_UP:
                return gamepad.dpad_up;
            case DPAD_DOWN:
                return gamepad.dpad_down;
            case DPAD_RIGHT:
                return gamepad.dpad_right;
            case RIGHT_BUMPER:
                return gamepad.right_bumper;
            case LEFT_BUMPER:
                return gamepad.left_bumper;
            case LEFT_STICK:
                return gamepad.left_stick_x > 0.1 && gamepad.left_stick_y > 0.1;
            case RIGHT_STICK:
                return gamepad.right_stick_x > 0.1 && gamepad.right_stick_y > 0.1;
            case TOUCHPAD:
                return gamepad.touchpad;
            default:
                return false;
        }
    }
}
