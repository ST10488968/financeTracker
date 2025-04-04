package vcmsa.ci.financetracker

import android.os.Bundle
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val incomeInput = findViewById<EditText>(R.id.incomeInput)
        val expenseFood = findViewById<EditText>(R.id.expenseFood)
        val expenseEntertainment = findViewById<EditText>(R.id.expenseEntertainment)
        val expenseBills = findViewById<EditText>(R.id.expenseBills)
        val expenseOther = findViewById<EditText>(R.id.expenseOther)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val resultText = findViewById<TextView>(R.id.resultText)
        val expenseFeedback = findViewById<TextView>(R.id.expenseFeedback)

        btnCalculate.setOnClickListener {
            val income = incomeInput.text.toString().toDoubleOrNull() ?: 0.0
            val foodExpense = expenseFood.text.toString().toDoubleOrNull() ?: 0.0
            val entertainmentExpense = expenseEntertainment.text.toString().toDoubleOrNull() ?: 0.0
            val billsExpense = expenseBills.text.toString().toDoubleOrNull() ?: 0.0
            val otherExpense = expenseOther.text.toString().toDoubleOrNull() ?: 0.0

            if (income == null || income <= 0) {
                Toast.makeText(this, "Please enter a valid income", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totalExpenses = foodExpense + entertainmentExpense + billsExpense + otherExpense
            val balance = income - totalExpenses

            resultText.text = "Total Income: $${String.format("%.2f", income)}\n" +
                    "Total Expenses: $${String.format("%.2f", totalExpenses)}\n" +
                    "Balance: $${String.format("%.2f", balance)}"

            if (balance >= 0) {
                resultText.append("\nYou are saving money")
                resultText.setTextColor(Color.GREEN)
            } else {
               resultText.append("\nYou are overspending!")
               resultText.setTextColor(Color.RED)
            }
            fun analyzeExpense(expense: Double, category: String) {
                val percentage = (expense / income) * 100
                when {
                    percentage > 30 -> expenseFeedback.append("\nWarning: $category expenses are too high! (${String.format("%.2f", percentage)}%)")
                    percentage < 5 -> expenseFeedback.append("\nGood Job: $category expenses are low (${String.format("%.2f", percentage)}%)")
                    else -> expenseFeedback.append("\n$category expenses are reasonable (${String.format("%.2f", percentage)}%)")
                }
            }

            expenseFeedback.text = ""
            analyzeExpense(foodExpense, "Food")
            analyzeExpense(entertainmentExpense, "Entertainment")
            analyzeExpense(billsExpense, "Bills")
            analyzeExpense(otherExpense, "Other")

            val totalExpensePercentage = (totalExpenses / income) * 100
            when {
                totalExpensePercentage > 30 -> {
                    expenseFeedback.append("\nOverall: Your expenses are too high!")
                    expenseFeedback.setTextColor(Color.RED)
                }
                totalExpensePercentage < 5 -> {
                    expenseFeedback.append("\nOverall: Your expenses are responsibly managed")
                    expenseFeedback.setTextColor(Color.GREEN)
            }
                else -> {
                    expenseFeedback.append("\nYour expenses are reasonable")
                    expenseFeedback.setTextColor(Color.BLACK)
                }
            }
        }
    }
}