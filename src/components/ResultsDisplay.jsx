import React from 'react';

const ResultsDisplay = ({ results, loading, error }) => {
  if (loading) {
    return (
      <div className="results-container">
        <div className="loading">Computing prime system results...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="results-container">
        <div className="error">
          <h3>Error</h3>
          <p>{error}</p>
        </div>
      </div>
    );
  }

  if (!results) {
    return (
      <div className="results-container">
        <div className="placeholder">
          <p>Enter character attributes and click "Calculate Prime System" to see results.</p>
        </div>
      </div>
    );
  }

  // Key mapping for display
  const outputKeyMap = {
    BaseAttack: 'Base Atk',
    BaseAtk: 'Base Atk',
    BaseAttack: 'Base Atk',
    Base_Attack: 'Base Atk',
    Base_Defense: 'Base Def',
    BaseDefense: 'Base Def',
    BaseDef: 'Base Def',
    HP: 'HP',
    SP: 'SP',
    MP: 'MP',
    HPRegen: 'HP Regen',
    SPRegen: 'SP Regen',
    MPRegen: 'MP Regen',
    DP: 'DP',
    // Add more mappings as needed
  };

  // Helper to format output keys
  const formatKey = (key) => {
    if (outputKeyMap[key]) return outputKeyMap[key];
    // Normalize key for common variants
    if (key.replace(/\s|_/g, '').toLowerCase() === 'baseattack') return 'Base Atk';
    if (key.replace(/\s|_/g, '').toLowerCase() === 'basedefense') return 'Base Def';
    // Capitalize HP/SP/MP if present
    if (/^(HP|SP|MP)$/i.test(key)) return key.toUpperCase();
    // Format regen keys
    if (/^(HP|SP|MP)Regen$/i.test(key)) return key.replace('Regen', ' Regen').toUpperCase();
    // Default: capitalize first letter
    return key.charAt(0).toUpperCase() + key.slice(1);
  };

  // Helper to format output values
  const formatValue = (value) => {
    if (typeof value === 'number') {
      // Show integer if no decimal, else show up to 2 decimals (no trailing .00)
      return Number.isInteger(value) ? value : value.toFixed(2).replace(/\.00$/, '');
    }
    return String(value);
  };

  // Handle different result types (compute, example, trySphere)
  const renderComputeResult = (result) => (
    <div className="compute-result">
      <h3>Prime System Calculation</h3>
      <div className="result-details">
        <div className="result-row">
          <span className="label">Role:</span>
          <span className="value">{result.role}</span>
        </div>
        <div className="result-row">
          <span className="label">Aspect:</span>
          <span className="value">{result.aspect}</span>
        </div>
        <div className="result-row">
          <span className="label">System Flavor:</span>
          <span className="value">{result.flavor}</span>
        </div>
        <div className="result-row">
          <span className="label">Formula Version:</span>
          <span className="value">{result.formulaVersion}</span>
        </div>
        <div className="result-row">
          <span className="label">Full Tank:</span>
          <span className="value">{result.fullTank ? 'Yes' : 'No'}</span>
        </div>
        <div className="result-row">
          <span className="label">Beyond Mortal:</span>
          <span className="value">{result.beyondMortal ? 'Yes' : 'No'}</span>
        </div>
        <div className="result-row">
          <span className="label">Resource:</span>
          <span className="value">{result.resourceName}</span>
        </div>
      </div>

      {result.output && (
        <div className="output-section">
          <h4>Computed Output</h4>
          <div className="output-details two-column-grid">
            {Object.entries(result.output).map(([key, value]) => {
              if (key === 'DP' && !result.showDP) return null; // Hide DP unless prompted
              return (
                <div key={key} className="result-row">
                  <span className="label">{formatKey(key)}:</span>
                  <span className="value">
                    {formatValue(value)}
                  </span>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );

  const renderExampleResult = (result) => (
    <div className="example-result">
      <h3>System Comparison</h3>
      <div className="comparison-details">
        <div className="result-row">
          <span className="label">Role:</span>
          <span className="value">{result.role}</span>
        </div>
        <div className="result-row">
          <span className="label">Aspect:</span>
          <span className="value">{result.aspect}</span>
        </div>
        <div className="result-row">
          <span className="label">Character Type:</span>
          <span className="value">{result.characterType}</span>
        </div>
      </div>

      <div className="comparison-grid">
        {result.sphere && (
          <div className="system-result">
            <h4>Sphere System</h4>
            {renderComputeResult(result.sphere)}
          </div>
        )}

        {result.legacy && (
          <div className="system-result">
            <h4>Legacy System</h4>
            {renderComputeResult(result.legacy)}
          </div>
        )}
      </div>
    </div>
  );

  // Determine result type and render accordingly
  if (results.sphere && results.legacy) {
    return (
      <div className="results-container">
        {renderExampleResult(results)}
      </div>
    );
  } else {
    return (
      <div className="results-container">
        {renderComputeResult(results)}
      </div>
    );
  }
};

export default ResultsDisplay;
