package com.example.demo.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1")
public class DemoDtoController {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private AtomicInteger tableCount = new AtomicInteger(1); 

    @PostMapping("/upload")
    public ResponseEntity<String> saveData(@RequestBody String json){
        try {
            JsonNode rootNode = new ObjectMapper().readTree(json);
            String tablename =  "dataset" + tableCount.getAndIncrement();
            generateCreateTableStatements(rootNode,tablename);
            insertData(rootNode, tablename);
            return new ResponseEntity<>("Table creation script generated successfully.", HttpStatus.OK);
            
        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error processing JSON", HttpStatus.BAD_REQUEST); 
        }
    }
    
    private Object getNodeValue(JsonNode value) {
        if (value.isTextual()) {
            return value.asText();
        }
        else if (value.isInt()) {
            return value.asInt();
        }
        else if (value.isDouble()) {
            return value.asDouble();
        }
        else if (value.isBoolean()) {
            return value.asBoolean();
        }
        else if (value.isLong()) {
            return value.asLong();
        }
        else {
            return null;
        }
    }
    
    private String mapJsonTypeToSql(JsonNode value) {
        if (value.isTextual() && value.asText().length() > 100) {
            return "TEXT";
        } else if (value.isTextual()) {
            return "VARCHAR(255)";
        } else if (value.isInt()) {
            return "INT";
        } else if (value.isDouble()) {
            return "FLOAT";
        }
        return "TEXT";
    }
    
    private void generateCreateTableStatements(JsonNode node, String tableName) {
    	StringBuilder createTableStatement = new StringBuilder("CREATE TABLE " + tableName + " (");
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String columnName = field.getKey();
            JsonNode value = field.getValue();
            
            if (value.isObject()) {
                String newTableName = "nested" + tableCount.getAndIncrement();
                generateCreateTableStatements(value, newTableName);
                createTableStatement.append(columnName).append("_id INT");
            } else {
                String columnType = mapJsonTypeToSql(value);
                createTableStatement.append(columnName).append(" ").append(columnType);
            }
            
            if (fields.hasNext()) {
                createTableStatement.append(", ");
            }
        }
        
        createTableStatement.append(");");
        String createTableSQL = createTableStatement.toString();
        
        try {
            jdbcTemplate.execute(createTableSQL);
            System.out.println("Table created successfully: " + tableName);
        } catch (Exception e) {
            System.err.println("Failed to create table: " + tableName);
            e.printStackTrace();
        }
    }
    
    private void insertData(JsonNode node, String tableName) {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String columnName = field.getKey();
            JsonNode value = field.getValue();

            if (!value.isObject() && !value.isArray()) {
                columnNames.append(columnName).append(",");
                placeholders.append(":" + columnName).append(",");
                parameters.addValue(columnName, getNodeValue(value)); // Assuming getNodeValue() correctly returns the value
            }
        }

        if (columnNames.length() > 0 && placeholders.length() > 0) {
            String sqlColumnNames = columnNames.substring(0, columnNames.length() - 1);
            String sqlPlaceholders = placeholders.substring(0, placeholders.length() - 1);

            String insertSql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, sqlColumnNames, sqlPlaceholders);
            namedParameterJdbcTemplate.update(insertSql, parameters);
            System.out.println("Data inserted successfully into: " + tableName);
        }
    }
}