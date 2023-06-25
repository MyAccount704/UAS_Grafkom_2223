package org.lwjglb.engine.scene;

import org.joml.*;
import org.lwjglb.engine.MathUtils;

import java.lang.Math;

public class Camera {

    private Vector3f direction;
    private Vector3f position;
    private Vector3f right;
    private Vector2f rotation;
    private Vector3f up;
    private Matrix4f viewMatrix;
    private Vector3f targetPosition = new Vector3f(0, 0, 0);

    public Camera() {
        direction = new Vector3f();
        right = new Vector3f();
        up = new Vector3f();
        position = new Vector3f();
        viewMatrix = new Matrix4f();
        rotation = new Vector2f();
    }

    public void addRotation(float x, float y) {
        rotation.add(x, y);
        recalculate();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void moveBackwards(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.sub(direction);
        recalculate();
    }
    public void moveDown(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.sub(up);
        recalculate();
    }
    public void moveForward(float inc) {
        viewMatrix.positiveZ(direction).negate().mul(inc);
        position.add(direction);
        recalculate();
    }
    public void moveLeft(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.sub(right);
        recalculate();
    }
    public void moveRight(float inc) {
        viewMatrix.positiveX(right).mul(inc);
        position.add(right);
        recalculate();
    }
    public void moveUp(float inc) {
        viewMatrix.positiveY(up).mul(inc);
        position.add(up);
        recalculate();
    }
    public void lockInEye() {
        rotation.x = (float) Math.toDegrees(Math.asin(-direction.y));
        rotation.y = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
        recalculate();
    }
    private void recalculate() {
        viewMatrix.identity()
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .translate(-position.x, -position.y, -position.z);
    }
    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        recalculate();
    }
    public void setRotation(float x, float y) {
        rotation.set(x, y);
        recalculate();
    }
    public Vector3f getDirection() {
        return direction;
    }
    public Vector3f getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector3f targetPosition) {
        this.targetPosition = targetPosition;
    }
    public void updatePosition() {
        // calculate the new camera position using lerp
        float lerpFactor = 0.05f; // adjust this value to control the speed of the camera movement
        Vector3f newCameraPosition = new Vector3f();
        newCameraPosition.x = MathUtils.lerp(position.x, targetPosition.x, lerpFactor);
        newCameraPosition.y = MathUtils.lerp(position.y, targetPosition.y, lerpFactor);
        newCameraPosition.z = MathUtils.lerp(position.z, targetPosition.z, lerpFactor);

        // update the camera position
        position.set(newCameraPosition);

        // calculate the view matrix and projection matrix
        recalculate();
    }
}