import React, { useState } from 'react';
import { enums } from '../services/api';

const AttributeForm = ({ onSubmit, loading }) => {
  const [formData, setFormData] = useState({
    // Attributes
    power: 650,
    vitality: 555,
    endurance: 560,
    mind: 700,
    self: 375,
    intent: 425,
    divine: 0,
    force: -1,
    soul: -1,
    spirit: -1,
    // System parameters
    role: 'MAGE',
    aspect: 'MAGIC',
    flavor: 'SPHERE',
    fullTank: false,
    formulaVersion: 'sphere-2.0',
    beyondMortal: false,
    characterType: 'HUMANOID',
    allowNonSentient: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked :
               type === 'number' ? parseFloat(value) : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="attribute-form">
      <h2>Character Attributes</h2>

      <div className="form-section">
        <h3>Core Attributes</h3>
        <div className="form-grid two-column-grid">
          <div className="column">
            <div className="form-group">
              <label htmlFor="power">Power:</label>
              <input
                type="number"
                id="power"
                name="power"
                value={formData.power}
                onChange={handleChange}
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label htmlFor="vitality">Vitality:</label>
              <input
                type="number"
                id="vitality"
                name="vitality"
                value={formData.vitality}
                onChange={handleChange}
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label htmlFor="endurance">Endurance:</label>
              <input
                type="number"
                id="endurance"
                name="endurance"
                value={formData.endurance}
                onChange={handleChange}
                step="0.1"
              />
            </div>
          </div>
          <div className="column">
            <div className="form-group">
              <label htmlFor="mind">Mind:</label>
              <input
                type="number"
                id="mind"
                name="mind"
                value={formData.mind}
                onChange={handleChange}
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label htmlFor="self">Self:</label>
              <input
                type="number"
                id="self"
                name="self"
                value={formData.self}
                onChange={handleChange}
                step="0.1"
              />
            </div>
            <div className="form-group">
              <label htmlFor="intent">Intent:</label>
              <input
                type="number"
                id="intent"
                name="intent"
                value={formData.intent}
                onChange={handleChange}
                step="0.1"
              />
            </div>
          </div>
        </div>
      </div>

      {/* <div className="form-section">
        <h3>Advanced Attributes</h3>
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="divine">Divine:</label>
            <input
              type="number"
              id="divine"
              name="divine"
              value={formData.divine}
              onChange={handleChange}
              step="0.1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="force">Force:</label>
            <input
              type="number"
              id="force"
              name="force"
              value={formData.force}
              onChange={handleChange}
              step="0.1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="soul">Soul:</label>
            <input
              type="number"
              id="soul"
              name="soul"
              value={formData.soul}
              onChange={handleChange}
              step="0.1"
            />
          </div>

          <div className="form-group">
            <label htmlFor="spirit">Spirit:</label>
            <input
              type="number"
              id="spirit"
              name="spirit"
              value={formData.spirit}
              onChange={handleChange}
              step="0.1"
            />
          </div>
        </div>
      </div> */}

      <div className="form-section">
        <h3>System Configuration</h3>
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="role">Role:</label>
            <select
              id="role"
              name="role"
              value={formData.role}
              onChange={handleChange}
            >
              {enums.Role.map(role => (
                <option key={role} value={role}>{role}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="aspect">Aspect:</label>
            <select
              id="aspect"
              name="aspect"
              value={formData.aspect}
              onChange={handleChange}
            >
              {enums.Aspect.map(aspect => (
                <option key={aspect} value={aspect}>{aspect}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="flavor">System Flavor:</label>
            <select
              id="flavor"
              name="flavor"
              value={formData.flavor}
              onChange={handleChange}
            >
              {enums.SystemFlavor.map(flavor => (
                <option key={flavor} value={flavor}>{flavor}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="characterType">Character Type:</label>
            <select
              id="characterType"
              name="characterType"
              value={formData.characterType}
              onChange={handleChange}
            >
              {enums.CharacterType.map(type => (
                <option key={type} value={type}>{type}</option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="formulaVersion">Formula Version:</label>
            <input
              type="text"
              id="formulaVersion"
              name="formulaVersion"
              value={formData.formulaVersion}
              onChange={handleChange}
            />
          </div>
        </div>
      </div>

      <div className="form-section">
        <h3>Options</h3>
        <div className="checkbox-group">
          <label>
            <input
              type="checkbox"
              name="fullTank"
              checked={formData.fullTank}
              onChange={handleChange}
            />
            Full Tank
          </label>

          <label>
            <input
              type="checkbox"
              name="beyondMortal"
              checked={formData.beyondMortal}
              onChange={handleChange}
            />
            Beyond Mortal
          </label>

          <label>
            <input
              type="checkbox"
              name="allowNonSentient"
              checked={formData.allowNonSentient}
              onChange={handleChange}
            />
            Allow Non-Sentient
          </label>
        </div>
      </div>

      <button type="submit" disabled={loading} className="submit-button">
        {loading ? 'Computing...' : 'Calculate Prime System'}
      </button>
    </form>
  );
};

export default AttributeForm;
