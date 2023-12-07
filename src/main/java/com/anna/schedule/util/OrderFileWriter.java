package com.anna.schedule.util;

import com.anna.schedule.PersonInterface;
import com.anna.schedule.order.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderFileWriter {

    private XSSFWorkbook workbook = new XSSFWorkbook();

    private XSSFSheet sheet;

    private int rowCount;

    public Workbook generate(List<Order> orderList,
                             PersonInterface person) {
        sheet = workbook.createSheet(person.getLastName() + " " + person.getFirstName() + ".xls");
        createHeader();
        fillSchedule(orderList, person);

        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    private void createHeader() {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(" ");

        cell = row.createCell(1);
        cell.setCellValue("Coworker");

        cell = row.createCell(2);
        cell.setCellValue("Coworker phone number");

        cell = row.createCell(3);
        cell.setCellValue("Order start");

        cell = row.createCell(4);
        cell.setCellValue("Order end");

        rowCount++;
    }

    private void fillSchedule(List<Order> orderList, PersonInterface person) {
        CellStyle cellStyleDate = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        cellStyleDate.setDataFormat(creationHelper.createDataFormat().getFormat("dd.mm.yyyy hh:MM"));

        Row row;
        Cell cell;
        PersonInterface coworker = null;

        orderList = orderList.stream()
                .sorted(Comparator.comparing(Order::getStartTime))
                .collect(Collectors.toList());

        for (Order order : orderList) {
            if (order.getEmployee() != null
                    && order.getEmployee().getId() == person.getId()) {
                coworker = order.getEmployer();
            } else if (order.getEmployee() != null
                    && order.getEmployer().getId() == person.getId()) {
                coworker = order.getEmployee();
            }

            row = sheet.createRow(rowCount);
            cell = row.createCell(0);
            cell.setCellValue(rowCount);

            if (coworker != null) {
                cell = row.createCell(1);
                cell.setCellValue(coworker.getLastName() + " " + coworker.getFirstName());
                cell = row.createCell(2);
                cell.setCellValue(coworker.getPhoneNumber());
            }

            coworker = null;

            cell = row.createCell(3);
            cell.setCellValue(order.getStartTime());
            cell.setCellStyle(cellStyleDate);

            cell = row.createCell(4);
            cell.setCellValue(order.getEndTime());
            cell.setCellStyle(cellStyleDate);

            rowCount++;
        }
    }
}
