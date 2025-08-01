package id.sample.cleancodeexercise

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var remainingBalanceText: TextView
    private lateinit var amountInput: EditText
    private lateinit var taxText: TextView
    private lateinit var totalText: TextView
    private lateinit var submitBtn: Button
    private var balance = 100000.0
    private var groupingUsed:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remainingBalanceText = findViewById(R.id.remainingBalanceText)
        amountInput = findViewById(R.id.amountInput)
        amountInput.setOnKeyListener { view, i, keyEvent ->  onAmountUpdated(); false }
        taxText = findViewById(R.id.taxText)
        totalText = findViewById(R.id.totalText)
        submitBtn = findViewById(R.id.submitBtn)
        submitBtn.setOnClickListener { onSubmitClicked() }

        remainingBalanceText.text = "Rp 100.000,00"
    }

    fun onAmountUpdated() {
        val a = amountInput.text.toString().toDoubleOrNull() ?: 0.0
        val nf = NumberFormat.getNumberInstance(Locale("in", "ID"))
        (nf as? DecimalFormat)?.apply {
            groupingUsed = true; decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator='.'; decimalSeparator=','
        }
            minimumFractionDigits = 2; maximumFractionDigits = 2
        }
        taxText.text = "Rp " + nf.format(a * 0.1)
        totalText.text = "Rp " + nf.format(a * 1.1)
    }

    fun onSubmitClicked() {
        val nf = NumberFormat.getNumberInstance(Locale("in","ID"))
        // repeated formatting configuration ...
        val rem = nf.parse(remainingBalanceText.text.toString().replace("Rp","").replace(".",""))?.toDouble() ?: 0.0
        val tot = nf.parse(totalText.text.toString().replace("Rp","").replace(".",""))?.toDouble() ?: 0.0
        if (tot > rem) {
            AlertDialog.Builder(this)
                .setTitle("Balance Insufficient")
                .setMessage("You have insufficient balance. Please top up.")
                .setPositiveButton("OK", null).show()
        } else {
            // complex 20 lines backend submission...
            AlertDialog.Builder(this)
                .setTitle("Payment Submitted!").setMessage("Your payment has been submitted!")
                .setPositiveButton("OK") { _, _ ->
                    amountInput.text.clear()
                    taxText.text = "N/A"
                    totalText.text = "N/A"
                    balance = rem - tot
                    remainingBalanceText.text = "Rp " + nf.format(balance)
                }
                .show()
        }
    }
}

