package org.lwjglb.game;

import org.joml.*;
import org.lwjglb.engine.*;
import org.lwjglb.engine.Object;
import org.lwjglb.engine.ShaderProgram;
import org.lwjglb.engine.graph.*;
import org.lwjglb.engine.scene.*;
import org.lwjglb.engine.scene.lights.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Main implements IAppLogic {
    private AnimationData animationData;
    private float lightAngle;
    private boolean FPP, TPP, freeCamera, Transition, toggleKeyPressed;
    ShaderProgram objectShader;
    Object bob;
    Vector3f pos = new Vector3f();

    public static void main(String[] args) {
        Main main = new Main();
        Window.WindowOptions opts = new Window.WindowOptions();
        opts.antiAliasing = true;
        Engine gameEng = new Engine("Rich Guy Holiday", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }
    @Override
    public void init(Window window, Scene scene, Render render) {
        FPP = true; TPP = false; freeCamera = false; Transition = false; toggleKeyPressed = false;

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER));

        objectShader = new ShaderProgram(shaderModuleDataList);
        objectShader.link();

        bob = new Sphere(shaderModuleDataList,
                new ArrayList<>(),
                new Vector4f(1f, 1f, 1f, 1f),
                Arrays.asList(0f, 0f, 0f),
                0.5f, 1f, 0.5f,
                36, 72)
                .inlineTranslateObject(1f, 3f, 30f);

        String terrainModelId = "terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/terrain.obj",
                scene.getTextureCache(), false);
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(100.0f);
        terrainEntity.setPosition(0f,0.5f,0f);
        terrainEntity.setRotation(1f, 0f, 0f, (float)Math.toRadians(90));
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);

        String environmentModelId = "environment";
        Model environmentModel = ModelLoader.loadModel(environmentModelId,   "resources/models/environment/zoo.obj",
                scene.getTextureCache(), false);
        scene.addModel(environmentModel);
        Entity environmentEntity = new Entity("environmentEntity", environmentModelId);
        environmentEntity.setScale(0.2f);
        environmentEntity.setPosition(0f,1f,0f);
        environmentEntity.setRotation(0f,-1f,0f,(float)Math.toRadians(90));
        environmentEntity.updateModelMatrix();
        scene.addEntity(environmentEntity);

        String benchModelId = "bench";
        Model benchModel = ModelLoader.loadModel(benchModelId,   "resources/models/bench/WoodBench.fbx",
                scene.getTextureCache(), false);
        scene.addModel(benchModel);
        Entity bench1Entity = new Entity("bench1Entity", benchModelId);
        bench1Entity.setScale(0.03f);
        bench1Entity.setPosition(-4.5f,0.8f,0f);
        bench1Entity.setRotation(-1f,0f,0f,(float)Math.toRadians(90));
        bench1Entity.updateModelMatrix();
        scene.addEntity(bench1Entity);
        Entity bench2Entity = new Entity("bench2Entity", benchModelId);
        bench2Entity.setScale(0.03f);
        bench2Entity.setPosition(14f,0.8f,0f);
        bench2Entity.setRotation(-1f,0f,0f,(float)Math.toRadians(90));
        bench2Entity.updateModelMatrix();
        scene.addEntity(bench2Entity);

        String treeModelId = "tree";
        Model treeModel = ModelLoader.loadModel(treeModelId,   "resources/models/tree/Oldtree.fbx",
                scene.getTextureCache(), false);
        scene.addModel(treeModel);
        Entity tree1Entity = new Entity("tree1Entity", treeModelId);
        tree1Entity.setScale(0.01f);
        tree1Entity.setPosition(20f,1f,-2.5f);
        tree1Entity.setRotation(0f,1f,0f,(float)Math.toRadians(90));
        tree1Entity.updateModelMatrix();
        scene.addEntity(tree1Entity);
        Entity tree2Entity = new Entity("tree2Entity", treeModelId);
        tree2Entity.setScale(0.015f);
        tree2Entity.setPosition(20f,2f,-40f);
        tree2Entity.setRotation(0f,1f,0f,(float)Math.toRadians(180));
        tree2Entity.updateModelMatrix();
        scene.addEntity(tree2Entity);
        Entity tree3Entity = new Entity("tree3Entity", treeModelId);
        tree3Entity.setScale(0.015f);
        tree3Entity.setPosition(-15f,1f,-20f);
        tree3Entity.setRotation(0f,1f,0f,(float)Math.toRadians(180));
        tree3Entity.updateModelMatrix();
        scene.addEntity(tree3Entity);

        String bobModelId = "bobModel";
        Model bobModel = ModelLoader.loadModel(bobModelId, "resources/models/bob/boblamp.md5mesh",
                scene.getTextureCache(), true);
        scene.addModel(bobModel);
        Entity bobEntity = new Entity("bobEntity", bobModelId);
        bobEntity.setScale(0.05f);
        bobEntity.setPosition(-8f,0.9f,12.5f);
        bobEntity.updateModelMatrix();
        animationData = new AnimationData(bobModel.getAnimationList().get(0));
        bobEntity.setAnimationData(animationData);
        scene.addEntity(bobEntity);

        String busModelId = "bus";
        Model busModel = ModelLoader.loadModel(busModelId, "resources/models/bus/bus1.obj",
                scene.getTextureCache(), false);
        scene.addModel(busModel);
        Entity busEntity = new Entity("busEntity", busModelId);
        busEntity.setScale(1.5f);
        busEntity.setPosition(-12f,0.9f,10f);
        busEntity.setRotation(0f,1f,0f,(float)Math.toRadians(90));
        busEntity.updateModelMatrix();
        scene.addEntity(busEntity);

        String mooseModelId = "mooseModel";
        Model mooseModel = ModelLoader.loadModel(mooseModelId, "resources/models/moose/DEER-Bleat.fbx",
                scene.getTextureCache(), true);
        scene.addModel(mooseModel);
        Entity mooseEntity = new Entity("mooseEntity", mooseModelId);
        mooseEntity.setScale(0.025f);
        mooseEntity.setPosition(-18f,0.6f,-12f);
        mooseEntity.setRotation(0f,-1f,0f,(float)Math.toRadians(135));
        mooseEntity.updateModelMatrix();
        animationData = new AnimationData(mooseModel.getAnimationList().get(0));
        mooseEntity.setAnimationData(animationData);
        scene.addEntity(mooseEntity);

        String bysonModelId = "byson";
        Model bysonModel = ModelLoader.loadModel(bysonModelId, "resources/models/byson/Bison.obj",
                scene.getTextureCache(), false);
        scene.addModel(bysonModel);
        Entity bysonEntity = new Entity("bysonEntity", bysonModelId);
        bysonEntity.setScale(5.7f);
        bysonEntity.setPosition(-16f,2.4f,-30f);
        bysonEntity.setRotation(0f,-1f,0f,(float)Math.toRadians(45));
        bysonEntity.updateModelMatrix();
        scene.addEntity(bysonEntity);

        String coyoteModelId = "coyote";
        Model coyoteModel = ModelLoader.loadModel(coyoteModelId, "resources/models/coyote/coyote.obj",
                scene.getTextureCache(), false);
        scene.addModel(coyoteModel);
        Entity coyote1Entity = new Entity("coyote1Entity", coyoteModelId);
        coyote1Entity.setScale(0.5f);
        coyote1Entity.setPosition(-5f,0.6f,-46f);
        coyote1Entity.updateModelMatrix();
        scene.addEntity(coyote1Entity);
        Entity coyote2Entity = new Entity("coyote2Entity", coyoteModelId);
        coyote2Entity.setScale(0.7f);
        coyote2Entity.setPosition(10f,0.6f,-43f);
        coyote2Entity.setRotation(0f,-1f,0f,(float)Math.toRadians(45));
        coyote2Entity.updateModelMatrix();
        scene.addEntity(coyote2Entity);

        String kongModelId = "kong";
        Model kongModel = ModelLoader.loadModel(kongModelId, "resources/models/kong/GorillaFigurine_Decimated.obj",
                scene.getTextureCache(), false);
        scene.addModel(kongModel);
        Entity kongEntity = new Entity("kongEntity", kongModelId);
        kongEntity.setScale(2.5f);
        kongEntity.setPosition(20f,16.6f,-33f);
        kongEntity.setRotation(-1.0f,0f,0f,(float)Math.toRadians(90));
        kongEntity.updateModelMatrix();
        scene.addEntity(kongEntity);

        String monkeModelId = "monke";
        Model monkeModel = ModelLoader.loadModel(monkeModelId, "resources/models/monke/12958_Spider_Monkey_v1_l2.obj",
                scene.getTextureCache(), false);
        scene.addModel(monkeModel);
        Entity monke1Entity = new Entity("monke1Entity", monkeModelId);
        monke1Entity.setScale(0.02f);
        monke1Entity.setPosition(20f,0.7f,-20f);
        monke1Entity.setRotation(-1.0f,0f,0f,(float)Math.toRadians(90));
        monke1Entity.updateModelMatrix();
        scene.addEntity(monke1Entity);
        Entity monke2Entity = new Entity("monke2Entity", monkeModelId);
        monke2Entity.setScale(0.04f);
        monke2Entity.setPosition(20f,0.7f,-10f);
        monke2Entity.setRotation(-1.0f,0f,0f,(float)Math.toRadians(90));
        monke2Entity.updateModelMatrix();
        scene.addEntity(monke2Entity);

        String kuroModelId = "kuroModel";
        Model kuroModel = ModelLoader.loadModel(kuroModelId, "resources/models/kuro/Loggerhead 18.fbx",
                scene.getTextureCache(), true);
        scene.addModel(kuroModel);
        Entity kuroEntity = new Entity("kuroEntity", kuroModelId);
        kuroEntity.setScale(0.04f);
        kuroEntity.setPosition(1.6f,2f,-20f);
        kuroEntity.setRotation(0f,1f,0f,(float)Math.toRadians(180));
        kuroEntity.updateModelMatrix();
        animationData = new AnimationData(kuroModel.getAnimationList().get(0));
        kuroEntity.setAnimationData(animationData);
        scene.addEntity(kuroEntity);

        String lampModelId = "lamp";
        Model lampModel = ModelLoader.loadModel(lampModelId,   "resources/models/lamp/street_lamp.fbx",
                scene.getTextureCache(), false);
        scene.addModel(lampModel);
        Entity lampEntity = new Entity("lampEntity", lampModelId);
        lampEntity.setScale(0.3f);
        lampEntity.setPosition(-8f,0f,2f);
        lampEntity.setRotation(0f,1f,0f,(float)Math.toRadians(90));
        lampEntity.updateModelMatrix();
        scene.addEntity(lampEntity);

        scene.addModel(lampModel);
        Entity lampEntity1 = new Entity("lampEntity1", lampModelId);
        lampEntity1.setScale(0.3f);
        lampEntity1.setPosition(10.5f,0f,2f);
        lampEntity1.setRotation(0f,1f,0f,(float)Math.toRadians(90));
        lampEntity1.updateModelMatrix();
        scene.addEntity(lampEntity1);

        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.3f);
        scene.setSceneLights(sceneLights);
        sceneLights.getPointLights().add(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(14.2f,8.5f,-37.65f), 50f));
        sceneLights.getPointLights().add(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(-12.5f,8.2f,-37.5f), 80f));
        sceneLights.getPointLights().add(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(-12f,9.1f,-0.35f), 10f));
        sceneLights.getPointLights().add(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(14.5f,9.1f,-0.35f), 10f));

        Vector3f coneDir = new Vector3f(0, 0, 1);
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(10.6f,0.98f,4f), 20f), coneDir, 360f));
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(0.5f, 0.5f, 0),
                new Vector3f(-8f,0.98f,4f), 20f), coneDir, 360f));


        SkyBox skyBox = new SkyBox("resources/models/skybox/skybox.obj", scene.getTextureCache());
        skyBox.getSkyBoxEntity().setScale(100);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);

//        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.02f));

        Camera camera = scene.getCamera();
        camera.setPosition(1f, 3f, 30f);

        lightAngle = 0;
    }

    @Override
    public void input(Window window, Scene scene) {
        float cameraSpeed = 0.2f;
        Camera camera = scene.getCamera();

        if (window.isKeyPressed(GLFW_KEY_1)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                Transition = true;

                FPP = true;
                TPP = false;
                freeCamera = false;
            }
        }
        else if (window.isKeyPressed(GLFW_KEY_2)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                Transition = true;

                FPP = false;
                TPP = true;
                freeCamera = false;
            }
        }
        else if (window.isKeyPressed(GLFW_KEY_3)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                Transition = true;

                FPP = false;
                TPP = false;
                freeCamera = true;

                pos = camera.getPosition();
            }
        }
        else if (!Transition){
            toggleKeyPressed = false;
        }

        if (window.isKeyPressed(GLFW_KEY_W)) {
            if (FPP || TPP) {
                bob.translateObject(0f, 0f, cameraSpeed);
            }
            camera.moveForward(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            if (FPP || TPP) {
                bob.translateObject(0f, 0f, -cameraSpeed);
            }
            camera.moveBackwards(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            if (FPP || TPP) {
                bob.translateObject(cameraSpeed, 0f, 0f);
            }
            camera.moveLeft(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            if (FPP || TPP) {
                bob.translateObject(-cameraSpeed, 0f, 0f);
            }
            camera.moveRight(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            camera.moveUp(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_CONTROL)) {
            camera.moveDown(cameraSpeed);
        }

        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            lightAngle -= 2.5f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            lightAngle += 2.5f;
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            if (FPP) {
                camera.addRotation(0, (float) Math.toRadians(displVec.y * 0.1f));
                bob.rotateObject((float) Math.toRadians(displVec.y * 0.1f), 0f, -1f, 0f);
            }
            else if (TPP){
                camera.addRotation((float) Math.toRadians(displVec.x * 0.1f), (float) Math.toRadians(displVec.y * 0.1f));
                bob.rotateObject((float) Math.toRadians(displVec.y * 0.1f), 0f, -1f, 0f);
            }
            else {
                camera.addRotation((float) Math.toRadians(displVec.x * 0.1f), (float) Math.toRadians(displVec.y * 0.1f));
            }
        }

//        SceneLights sceneLights = scene.getSceneLights();
//        DirLight dirLight = sceneLights.getDirLight();
//        double angRad = Math.toRadians(lightAngle);
//        dirLight.getDirection().z = (float) Math.sin(angRad);
//        dirLight.getDirection().y = (float) Math.cos(angRad);
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        animationData.nextFrame();
        cameraTransition(scene);
    }

    private void cameraTransition(Scene scene) {
        float acceptedOffset = 0.3f;
        Camera camera = scene.getCamera();
        if (!Transition)
            return;
        if (FPP) {
            Vector3f target = new Vector3f(
                    bob.getCenterPoint().get(0),
                    bob.getCenterPoint().get(1),
                    bob.getCenterPoint().get(2));

            camera.setTargetPosition(target);
            camera.updatePosition();

            if (camera.getPosition().distance(target) < acceptedOffset) {
                Transition = false;
            }
        }
        else if (TPP) {
            Vector3f target = new Vector3f(
                    bob.getCenterPoint().get(0),
                    bob.getCenterPoint().get(1) + 2.5f,
                    bob.getCenterPoint().get(2) + 2f);

            camera.setTargetPosition(target);
            camera.updatePosition();

            if (camera.getPosition().distance(target) < acceptedOffset) {
                Transition = false;
            }
        }
        else if (freeCamera) {
            Vector3f target = new Vector3f(
                    1.1f,
                    2f,
                    30f);

            camera.setTargetPosition(target);
            camera.updatePosition();

            if (camera.getPosition().distance(target) < acceptedOffset) {
                Transition = false;
            }
        }
    }
}