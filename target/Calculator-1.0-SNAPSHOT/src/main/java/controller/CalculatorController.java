package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FourArithmeticOperations;

@WebServlet("/calculator")
public class CalculatorController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //最初
        request.setAttribute("currentOperand", "0");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/calculator.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formulaStr = request.getParameter("formula");
        //数字箱
        String numbersStr = request.getParameter("numbers");
        //演算子箱
        String operatorsStr = request.getParameter("operators");
        
        List<Double> numbers = new ArrayList<>();
        if (numbersStr != null && !numbersStr.isEmpty()) {
            // カンマで配列に分割
            String[] numArray = numbersStr.split(",");
            for (int i = 0; i < numArray.length; i++) {
                numbers.add(Double.parseDouble(numArray[i]));
                
                request.setAttribute("previousOperand", formulaStr);
            }
        }
        
        List<String> operators = new ArrayList<>();
        if (operatorsStr != null && !operatorsStr.isEmpty()) {
            // カンマで配列に分割
            String[] opArray = operatorsStr.split(",");
            for (int i = 0; i < opArray.length; i++) {
                operators.add(opArray[i]);
            }
        }
        
        FourArithmeticOperations calc = new FourArithmeticOperations();
System.out.println("koetayo-");        
//-が頭にある場合
        if (numbers.size() == operators.size()) {
            numbers.add(0, 0.0);
            System.out.println("-");
        }

//掛け算割り算からしていく
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("/") || operators.get(i).equals("*")) {
                double num1 = numbers.get(i);
                double num2 = numbers.get(i + 1);
                double result = 0;
                
                if (operators.get(i).equals("/")) {
                    result = calc.division(num1, num2);
                } else {
                    result = calc.multiplication(num1, num2);
                }
                
                numbers.set(i, result);
                numbers.remove(i + 1);
                operators.remove(i);
                i--; // リストが縮んだ分、インデックスを1つ戻す
            }
        }
        
        // 残った足し算と引き算を計算
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("+") || operators.get(i).equals("-")) {
                double num1 = numbers.get(i);
                double num2 = numbers.get(i + 1);
                double result = 0;

                if (operators.get(i).equals("+")) {
                    result = calc.addition(num1, num2);
                } else {
                    result = calc.subtraction(num1, num2);
                }
                
                numbers.set(i, result);
                numbers.remove(i + 1);
                operators.remove(i);
                i--;
            }
        }
        //JSP返す用
        String currentOperandStr = String.valueOf(numbers.get(0));
        if(currentOperandStr.endsWith(".0")){
            currentOperandStr = currentOperandStr.substring(0, currentOperandStr.length() - 2);
            request.setAttribute("currentOperand", currentOperandStr);
            } else {
                request.setAttribute("currentOperand", currentOperandStr);
            }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/calculator.jsp");
        dispatcher.forward(request, response);
    }
}