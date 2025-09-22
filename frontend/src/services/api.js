const API_BASE_URL = 'http://localhost:8080/api/prime';

export const api = {
  // Compute prime system results
  compute: async (sheetInput) => {
    const response = await fetch(`${API_BASE_URL}/compute`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(sheetInput),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  },

  // Get example comparison between Sphere and Legacy
  getExample: async (params = {}) => {
    const queryParams = new URLSearchParams(params);
    const response = await fetch(`${API_BASE_URL}/example?${queryParams}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  },

  // Try sphere system with custom attributes
  trySphere: async (params = {}) => {
    const queryParams = new URLSearchParams(params);
    const response = await fetch(`${API_BASE_URL}/try-sphere?${queryParams}`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  },
};

// Enums from backend
export const enums = {
  Role: ['WARRIOR', 'MAGE', 'TANK', 'HYBRID'],
  Aspect: ['MAGIC', 'CHI', 'ENERGY', 'DIVINE', 'NONE'],
  SystemFlavor: ['LEGACY', 'SPHERE'],
  CharacterType: ['HUMANOID', 'CONSTRUCT', 'UNDEAD', 'DEMON', 'ANGEL'],
};
