package org.njuse.nekocoin.nekocoin.Utils;

import com.alibaba.fastjson.JSON;
import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import org.njuse.nekocoin.nekocoin.entity.Category;
import org.njuse.nekocoin.nekocoin.entity.Entity;
import org.njuse.nekocoin.nekocoin.entity.Graph;
import org.njuse.nekocoin.nekocoin.entity.Relation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestCaseAutoGenerate {
    public static Faker faker = new Faker(new Locale("zh-CN"));
    public static Entity generateEntity(String graphId){
        HashMap<String, Object> properties = new HashMap<>();
        Random random = new Random();
        String name = faker.harryPotter().character();
        Double x = faker.number().randomDouble(10000,-321,409);
        Double y = faker.number().randomDouble(10000,-321,409);
        Double symbolSize = faker.number().randomDouble( 10000,-321,409);
        int category = random.nextInt(9);
        long value = faker.number().randomNumber();
        String shape = "round";
        String color = faker.color().name();
        properties.put("graphId", graphId);
        properties.put("name", name);
        properties.put("color", color);
        properties.put("shape", shape);
        properties.put("x", x);
        properties.put("y", y);
        properties.put("category", category);
        properties.put("symbolSize", symbolSize);
        properties.put("value", value);
        return new Entity(properties);
    }

    public static List<Relation> generateRelation(Set<Entity> entities){
        List<Relation> relations = new ArrayList<>();
        List<Entity> entityList = new ArrayList<>(entities);
        int[] idx = new  int[100];
        for(int i = 0; i < 100; i++){
            idx[i] = i;
        }
        for(int i = 0; i < 10; i++){
            for(int j = 50; j < 60; j++){
                relations.add(new Relation(entityList.get(i).getName(), entityList.get(j).getName()
                        ,faker.harryPotter().location(),
                        entityList.get(0).getGraphId()));
            }
        }
        for(int i = 10; i < 20; i++){
            for(int j = 60; j < 70; j++){
                relations.add(new Relation(entityList.get(i).getName(), entityList.get(j).getName(),
                        faker.harryPotter().location(),
                        entityList.get(0).getGraphId()));
            }
        }
        for(int i = 20; i < 30; i++){
            for(int j = 70; j < 80; j++){
                relations.add(new Relation(entityList.get(i).getName(), entityList.get(j).getName(),
                        faker.harryPotter().location(),
                        entityList.get(0).getGraphId()));
            }
        }
        for(int i = 30; i < 40; i++){
            for(int j = 80; j < 90; j++){
                relations.add(new Relation(entityList.get(i).getName(), entityList.get(j).getName(),
                        faker.harryPotter().location(),
                        entityList.get(0).getGraphId()));
            }
        }
        for(int i = 40; i < 50; i++){
            for(int j = 90; j < 100; j++){
                relations.add(new Relation(entityList.get(i).getName(), entityList.get(j).getName(),
                        faker.harryPotter().location(),
                        entityList.get(0).getGraphId()));
            }
        }
        return relations;
    }

    public static List<Category> generateCategories(int bound, String graphId){
        List<Category> categories = new ArrayList<>();
        for(int i = 0; i < bound; i++){
            categories.add(new Category("类目" + i, graphId));
        }
        return categories;
    }
    public static Category generateCategory(String graphId){
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("name", faker.internet());
        properties.put("graphId", graphId);
        return  new Category(properties);
    }
    public static Graph generateGraph(){
        Map<String, Object> graphProperties = new HashMap<>();
        String graphId = faker.idNumber().toString();
        graphProperties.put("graphId", graphId);
        graphProperties.put("name", graphId);
        Graph graph = new Graph(graphProperties);
        return graph;
    }
    public static void writeTestFile(){
        File file = new File("C:\\Users\\12736\\Desktop\\study\\se3","testFile");
        String graphJson = JSON.toJSONString(generateTestGraphWithEntities());
        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static Graph generateTestGraphWithEntities(){
        Set<Entity> entities = new HashSet<>();
        while(entities.size() < 100){
            entities.add(generateEntity("ft"));
        }
        List<Relation> relations = generateRelation(entities);
        List<Category> categories = generateCategories(10, "ft");
        Graph graph = new Graph();
        graph.setEntities(new ArrayList<>(entities));
        graph.setLinks(relations);
        graph.setCategories(categories);
        graph.setName("testGraph");
        graph.setGraphId("ft");
        return graph;
    }
}
